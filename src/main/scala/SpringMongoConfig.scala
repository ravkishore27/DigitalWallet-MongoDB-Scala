import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
class SpringMongoConfig {
 
	@throws(classOf[Exception])
	@Bean
	def mongoDbFactory() : MongoDbFactory = {
		val uri: MongoClientURI=new MongoClientURI( "mongodb://RaviKishore:cmpe273@ds047720.mongolab.com:47720/cmpe273-assignment2")
		new SimpleMongoDbFactory(new MongoClient(uri), "cmpe273-assignment2");
	}
 
	@throws(classOf[Exception])
	@Bean
	def mongoTemplate() : 	MongoTemplate = {
 
//		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
 
//		return mongoTemplate;
		new MongoTemplate(mongoDbFactory())
 
	}
 
}

/**

@Configuration
class MongoConfig extends AbstractMongoConfiguration {
 
def getDatabaseName:String = "mydb"
 
def mongo:Mongo = new Mongo("localhost")
 
}

*/
