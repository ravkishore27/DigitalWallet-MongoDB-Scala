import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Entity
import scala.beans.BeanProperty
import org.hibernate.validator.constraints.NotEmpty

@Document(collection = "weblogins")
@Entity
class WebLogin {

	@BeanProperty
	var id: String = _

	@BeanProperty
	@NotEmpty
	var url: String = _
	  
	@BeanProperty
	@NotEmpty
	var login: String = _

	@BeanProperty
	@NotEmpty
	var password: String = _

	@BeanProperty
	var userId : String = _

	override def toString : String = {
		return "{ " + " id : " + id + ", " + "url : " + url + ", " + "login : " + login + ", " + "password : " +
				password + ", " + "userId : " + userId + " }"
	}
}
