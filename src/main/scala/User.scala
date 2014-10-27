import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Entity
import scala.beans.BeanProperty
import org.hibernate.validator.constraints.NotEmpty
import java.util.Date
import java.text.SimpleDateFormat
import java.text.DateFormat
//import java.util.Calendar
//import java.sql.Timestamp
//import javax.persistence.Id


@Document(collection = "users")
@Entity
class User {

	@Id  
	@BeanProperty
	var id: String = _

	@BeanProperty
	@NotEmpty
	var email: String = _
	  
	@BeanProperty
	@NotEmpty
	var password: String = _

	@BeanProperty
	var name: String = _

	@BeanProperty
	var created_at: String = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date())

	@BeanProperty
	var updated_at: String = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date())

	override def toString :String = {
		return "{ "+"id : " + id + ", " + "email : " + email + ", " + "password : " + password + ", " + "name : " + name + ", " + 					"created_at : " + created_at + ", " + "updated_at : " + updated_at + " }"
	}

}
