import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Entity
import scala.beans.BeanProperty
import org.hibernate.validator.constraints.NotEmpty

@Document(collection = "bankaccounts")
@Entity
class BankAccount {

	@BeanProperty
	var id: String = _

	@BeanProperty
	var account_name: String = _
	  
	@BeanProperty
	@NotEmpty
	var routing_number: String = _

	@BeanProperty
	@NotEmpty
	var account_number: String = _

	@BeanProperty
	var userId : String = _

	override def toString : String = {
		return "{ " + " id : " + id + ", " + "account_name : " + account_name + ", " + "routing_number : " + routing_number + ", " + 					"account_number : " +	account_number + ", " + "userId : " + userId + " }"
	}
}
