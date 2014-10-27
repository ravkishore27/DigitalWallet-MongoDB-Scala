import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Entity
import scala.beans.BeanProperty
import org.hibernate.validator.constraints.NotEmpty
import java.util.Date

@Document(collection = "idcards")
@Entity
class IDcard {

	@BeanProperty
	var id: String = _

	@BeanProperty
	@NotEmpty
	var cardName: String = _
	  
	@BeanProperty
	@NotEmpty
	var cardNumber: String = _

	@BeanProperty
	var expDate: Date = _

	@BeanProperty
	var userId : String = _

	override def toString : String = {
		return "{ " + " id : " + id + ", " + "cardName : " + cardName + ", " + "cardNumber : " + cardNumber + ", " + "expDate : " +
				expDate + ", " + "userId : " + userId + " }"
	}
}
