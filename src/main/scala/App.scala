import java.util.List;
 
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.MongoTemplate;

import collection.JavaConversions._
import org.springframework.stereotype.Controller
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation._
import org.springframework.boot.SpringApplication
//import javax.servlet.http.HttpServletRequest
import org.springframework.http._
import javax.validation.Valid

import org.springframework.web.client.RestTemplate
import org.springframework.http.MediaType
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.converter.AbstractHttpMessageConverter
import java.util.Collections
import java.util.Collections.singletonList
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


object App {
 
	def main(args : Array[String]) 
	{
		SpringApplication.run(classOf[App])
	}
}


@EnableAutoConfiguration
@Controller
@RequestMapping(Array("/api/v1/users"))
class App {
	val ctx : ApplicationContext = new AnnotationConfigApplicationContext(classOf[SpringMongoConfig])
	val mongoOperation : MongoOperations = ctx.getBean("mongoTemplate").asInstanceOf[MongoOperations]
	val restTemplate : RestTemplate = new RestTemplate()
	
	//ADD User
	@RequestMapping(method = Array(RequestMethod.POST))
	@ResponseStatus( HttpStatus.CREATED )
	@ResponseBody
	private def addUser(@Valid @RequestBody user : User)  : String = {
		mongoOperation.save(user, "users")
		user.toString
	}

	//GET user
	@RequestMapping(value=Array("{uid}"), method = Array(RequestMethod.GET))
	@ResponseBody
	private def viewUser(@PathVariable uid: String)  : ResponseEntity[String] = {
		var searchUserQuery = new Query(Criteria where("_id") is (uid))
		var user : User = mongoOperation.findOne(searchUserQuery, classOf[User], "users")
		if(user == null){
			return new ResponseEntity[String]("No user found for the mentioned User ID", HttpStatus.NOT_FOUND)
		}
		else{
			return new ResponseEntity[String](user.toString, HttpStatus.OK)
		}
	}

	//UPDATE user
	@RequestMapping(value=Array("{uid}"),method =Array(RequestMethod.PUT))
	@ResponseBody
	private def updateUser(@Valid @RequestBody user :User, @PathVariable uid: String) : ResponseEntity[String] = {
		var searchUserQuery = new Query(Criteria where ("_id") is (uid))
		var userFound : User = mongoOperation.findOne(searchUserQuery, classOf[User],"users")
		if(userFound == null){
			return new ResponseEntity[String]("No user found for the mentioned User ID", HttpStatus.NOT_FOUND)
		}
		else{
			if(user.getEmail != null){
			 	mongoOperation.updateFirst(searchUserQuery, Update.update("email",user.getEmail),classOf[User],"users")
			}
			if(user.getPassword != null){
				mongoOperation.updateFirst(searchUserQuery, Update.update("password",user.getPassword),classOf[User],"users")
			}
			if(user.getName != null){
				mongoOperation.updateFirst(searchUserQuery, Update.update("name",user.getName),classOf[User],"users")
			}
			mongoOperation.updateFirst(searchUserQuery, Update.update("updated_at",user.getUpdated_at),classOf[User],"users")
			userFound = mongoOperation.findOne(searchUserQuery, classOf[User],"users")
			return new ResponseEntity[String](userFound.toString, HttpStatus.OK)
		}
	}

	//ADD Idcards
	@RequestMapping(value=Array("{uid}/idcards"),method =Array(RequestMethod.POST))
	@ResponseBody
	def addIdCard(@Valid @RequestBody idcard :IDcard, @PathVariable uid:String) : ResponseEntity[String] = {
		var searchUserQuery = new Query(Criteria where("_id") is (uid))
		var user : User = mongoOperation.findOne(searchUserQuery, classOf[User], "users")
		if( user == null){
			return new ResponseEntity[String]("User ID does not exist", HttpStatus.NOT_FOUND)
		}
		else{
			idcard.setUserId(uid)
			mongoOperation.save(idcard, "idcards")
			return new ResponseEntity[String](idcard.toString , HttpStatus.CREATED)
		}
	  }

	//GET IDcards
	@RequestMapping(value=Array("{uid}/idcards"),method =Array(RequestMethod.GET))
	@ResponseBody
	def viewIDcards(@PathVariable uid: String) : ResponseEntity[String] = {
		var searchUserQuery = new Query(Criteria where("_id") is (uid))
		var user : User = mongoOperation.findOne(searchUserQuery, classOf[User], "users")
		if(user == null){
			return new ResponseEntity[String]("No user found for the mentioned User ID", HttpStatus.NOT_FOUND)
		}
		else{	
			searchUserQuery = new Query(Criteria where("userId") is (uid))
			var idcards_list : List[IDcard] = mongoOperation.find(searchUserQuery, classOf[IDcard], "idcards")	  
			if (idcards_list.isEmpty()){
				return new ResponseEntity[String]("No IDcards for this user", HttpStatus.NOT_FOUND);
			}
			else{
				var idcards : String = ""
				for(idcard <- idcards_list){
					idcards += idcard + " "
				}
				return new ResponseEntity[String](idcards , HttpStatus.OK)
			}
		}		
	}


	//DELETE IDcard
	@RequestMapping(value=Array("{uid}/idcards/{cid}"),method =Array(RequestMethod.DELETE))
	@ResponseBody
	def deleteIDcard(@PathVariable uid : String, @PathVariable cid : String) : ResponseEntity[String] = {
		var searchUserQuery = new Query(Criteria where("_id") is (uid))
		var user : User = mongoOperation.findOne(searchUserQuery, classOf[User], "users")
		if( user == null){
			return new ResponseEntity[String]("User ID does not exist", HttpStatus.NOT_FOUND)
		}else{
			searchUserQuery = new Query(Criteria where("_id") is (cid))
			var idcard = mongoOperation.findOne(searchUserQuery, classOf[IDcard], "idcards")
			if (idcard == null){
				return new ResponseEntity[String]("Card ID for this User does not exist", HttpStatus.NOT_FOUND);
			}
			else{
				mongoOperation.remove(searchUserQuery,classOf[IDcard],"idcards")
				return new ResponseEntity[String](HttpStatus.NO_CONTENT)
			}	
		}
	}


	//ADD WebLogins
	@RequestMapping(value=Array("{uid}/weblogins"),method =Array(RequestMethod.POST))
	@ResponseBody
	def addWebLogins(@Valid @RequestBody weblogin : WebLogin, @PathVariable uid:String) : ResponseEntity[String] = {
		var searchUserQuery = new Query(Criteria where("_id") is (uid))
		var user : User = mongoOperation.findOne(searchUserQuery, classOf[User], "users")
		if(user == null){
			return new ResponseEntity[String]("No user found for the mentioned User ID", HttpStatus.NOT_FOUND)
		}
		else{
				weblogin.setUserId(uid)
				mongoOperation.save(weblogin, "weblogins")
				return new ResponseEntity[String](weblogin.toString , HttpStatus.CREATED)
		}
	}


	//GET WebLogins
	@RequestMapping(value=Array("{uid}/weblogins"),method =Array(RequestMethod.GET))
	@ResponseBody
	def viewWebLogins(@PathVariable uid: String) : ResponseEntity[String] = {
		var searchUserQuery = new Query(Criteria where("_id") is (uid))
		var user : User = mongoOperation.findOne(searchUserQuery, classOf[User], "users")
		if(user == null){
			return new ResponseEntity[String]("No user found for the mentioned User ID", HttpStatus.NOT_FOUND)
		}
		else{
			searchUserQuery = new Query(Criteria where("userId") is (uid))
			var weblogin_list : List[WebLogin] = mongoOperation.find(searchUserQuery, classOf[WebLogin], "weblogins")
			if (weblogin_list.isEmpty()){
				return new ResponseEntity[String]("No Web logins for this User ID", HttpStatus.NOT_FOUND);
			}
			else{
				var weblogins : String = ""
				for(weblogin <- weblogin_list){
					weblogins += weblogin + " "
				}
				return new ResponseEntity[String](weblogins , HttpStatus.OK)
			}		
		}
	}


	//DELETE IDcard
	@RequestMapping(value=Array("{uid}/weblogins/{wid}"),method =Array(RequestMethod.DELETE))
	@ResponseBody
	def deleteWebLogin(@PathVariable uid : String, @PathVariable wid : String) : ResponseEntity[String] = {
		var searchUserQuery = new Query(Criteria where("_id") is (uid))
		var user : User = mongoOperation.findOne(searchUserQuery, classOf[User], "users")
		if(user == null){
			return new ResponseEntity[String]("No user found for the mentioned User ID", HttpStatus.NOT_FOUND)
		}
		else{
			searchUserQuery = new Query(Criteria where("_id") is (wid))
			var weblogin = mongoOperation.findOne(searchUserQuery, classOf[WebLogin], "weblogins")
			if (weblogin == null){
				return new ResponseEntity[String]("Weblogin ID for this User does not exist", HttpStatus.NOT_FOUND)
			}
			else{
				mongoOperation.remove(searchUserQuery,classOf[WebLogin],"weblogins")
				return new ResponseEntity[String](HttpStatus.NO_CONTENT)
			}	
		}
	}


	//ADD BankAccount
	@RequestMapping(value=Array("{uid}/bankaccounts"),method =Array(RequestMethod.POST))
	@ResponseBody
	def addBankAccount(@Valid @RequestBody  bankaccount : BankAccount, @PathVariable uid:String) : ResponseEntity[String] = {
		var searchUserQuery = new Query(Criteria where("_id") is (uid))
		var user : User = mongoOperation.findOne(searchUserQuery, classOf[User], "users")
	
		if(user == null){
			return new ResponseEntity[String]("No user found for the mentioned User ID", HttpStatus.NOT_FOUND)
		}
		else{

			var myString = restTemplate.getForObject("http://www.routingnumbers.info/api/data.json?rn="+bankaccount.getRouting_number, classOf[String])
			
			var parser : JSONParser = new JSONParser()
			var string = parser.parse(myString).asInstanceOf[JSONObject]
			
			print(string.get("code"))
			if (string.get("code").toString == "400"){
				return new ResponseEntity[String]("The supplied routing number doesn't exist", HttpStatus.NOT_FOUND)
			}
			else{
				var bankaccount_new : BankAccount = new BankAccount()
				bankaccount_new.setRouting_number(string.get("routing_number").toString)
				bankaccount_new.setAccount_name(string.get("customer_name").toString)
				bankaccount_new.setUserId(uid)
				mongoOperation.save(bankaccount_new, "bankaccounts")
				return new ResponseEntity[String](bankaccount_new.toString , HttpStatus.CREATED)
			}		
		}
	  }


	//GET BankAccounts
	@RequestMapping(value=Array("{uid}/bankaccounts"),method =Array(RequestMethod.GET))
	@ResponseBody
	def viewBankAccount(@PathVariable uid: String) : ResponseEntity[String] = {
		var searchUserQuery = new Query(Criteria where("_id") is (uid))
		var user : User = mongoOperation.findOne(searchUserQuery, classOf[User], "users")
		if(user == null){
			return new ResponseEntity[String]("No user found for the mentioned User ID", HttpStatus.NOT_FOUND)
		}
		else{
			searchUserQuery = new Query(Criteria where("userId") is (uid))
			var bankaccount_list : List[BankAccount] = mongoOperation.find(searchUserQuery, classOf[BankAccount], "bankaccounts")
			if (bankaccount_list.isEmpty()){
				return new ResponseEntity[String]("No Bank Accounts for this User ID", HttpStatus.NOT_FOUND);
			}
			else{
				var bankaccounts : String = ""
				for(bankaccount <- bankaccount_list){
					bankaccounts += bankaccount + " "
				}
				return new ResponseEntity[String](bankaccounts , HttpStatus.OK)
			}		
		}
	}


	//DELETE BankAccounts
	@RequestMapping(value=Array("{uid}/bankaccounts/{bid}"),method =Array(RequestMethod.DELETE))
	@ResponseBody
	def deleteBankAccount(@PathVariable uid : String, @PathVariable bid : String) : ResponseEntity[String] = {
		var searchUserQuery = new Query(Criteria where("_id") is (uid))
		var user : User = mongoOperation.findOne(searchUserQuery, classOf[User], "users")
		if(user == null){
			return new ResponseEntity[String]("No user found for the mentioned User ID", HttpStatus.NOT_FOUND)
		}
		else{
			searchUserQuery = new Query(Criteria where("_id") is (bid))
			var bankaccount = mongoOperation.findOne(searchUserQuery, classOf[BankAccount], "bankaccounts")
			if (bankaccount == null){
				return new ResponseEntity[String]("Bank Account ID for this User does not exist", HttpStatus.NOT_FOUND)
			}
			else{
				mongoOperation.remove(searchUserQuery,classOf[BankAccount],"bankaccounts")
				return new ResponseEntity[String](HttpStatus.NO_CONTENT)
			}	
		}
	}	
}
