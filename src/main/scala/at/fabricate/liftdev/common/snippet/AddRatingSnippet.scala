package at.fabricate.liftdev.common
package snippet

import model.AddRating
import model.AddRatingMeta
import net.liftweb.util.CssSel
import net.liftweb.http.js.JsCmds
import net.liftweb.http.js.jquery.JqJsCmds.AppendHtml
import net.liftweb.http.SHtml
import net.liftweb.util.Helpers._
import scala.xml.NodeSeq
import scala.xml.Text
import model.BaseEntity
import model.BaseMetaEntityWithTitleAndDescription
import model.BaseEntityWithTitleAndDescription
import net.liftweb.http.js.JsCmds.SetHtml
import net.liftweb.common.Empty

trait AddRatingSnippet[T <: BaseEntityWithTitleAndDescription[T] with AddRating[T]] extends BaseEntityWithTitleAndDescriptionSnippet[T] {
  
  var listOfRatingOptionsCSStoInt : List[(String,Int)] = List(
		  	"rating1"->1,
		  	"rating2"->2,
		  	"rating3"->3,
		  	"rating4"->4,
		  	"rating5"->5
		  	
		  )
  
 	 def bindNewRatingCSS(item : ItemType) : CssSel = {
 	   	 def createNewItem : item.TheRating = item.TheRating.create.ratedItem(item) 
		 
		 listOfRatingOptionsCSStoInt.map({
		   case (css, value) => {
		     val newRating = createNewItem
		   	 newRating.rating.set(value)
		     "#%s [onclick]".format(css) #> SHtml.ajaxInvoke(() => {
	       saveAndDisplayAjaxMessages(newRating,
	           () => {
				 // update the ratings
				SetHtml("rating", generateDisplayRating(item)) &
//				 // hide the form
				SetHtml("newrating",NodeSeq.Empty )
	           }, 
	           errors => {
	             errors.map(println(_))
	             JsCmds.Alert("adding rating failed! " )
	           },"ratingMessages")
			          } )
		   }
		 }).reduce(_ & _)
 	 }

  
  def generateDisplayRating(item : ItemType) : NodeSeq = {
//    if (item.accumulatedRatings == null) 
//      return Text("no ratings available")
    item.accumulatedRatings.get match {
      case 0.0 => Text("no ratings available")
      case someNumber if someNumber != null => Text("%1.2f".format(someNumber))
      case _ => Text("no ratings available")
    }
  }
  
  // maybe save that value as a precomputed field (addidtionally with a date) into the database in case of performance issues
  
  
  abstract override def asHtml(item : ItemType) : CssSel = {
		 
//		println("chaining asHtml from AddCommentSnippet")
     ("#rating" #> generateDisplayRating(item)  &
     "#newrating" #> bindNewRatingCSS(item)) &
     // chain the css selectors 
     (super.asHtml(item))
  }
}
