package at.fabricate
package snippet

import net.liftweb.common.Logger
import net.liftweb.http.DispatchSnippet
import model.User
import net.liftweb.http.S
import scala.xml.NodeSeq
import lib.MapperBinder
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.util.Helpers._
import net.liftweb.mapper.Descending
import net.liftweb.mapper.OrderBy
import net.liftweb.mapper.MaxRows
import net.liftweb.mapper.StartAt
import model.Project
import scala.xml.Text
import net.liftweb.http.SHtml
import at.fabricate.model.Tool

object UserSnippet extends BaseEntityWithTitleAndDescriptionSnippet[User] with BaseEntityWithTitleDescriptionAndIconSnippet[User]  {
  
  override val TheItem = User
  override def itemBaseUrl = "designer"
//    Dont change anything as it is hardcoded atm
//  override def itemViewUrl = "view"
//  override def itemListUrl = "list"
//  override def itemEditUrl = "edit"
  override def viewTemplate = "viewDesigner"
  override def listTemplate = "listDesigner"
  override def editTemplate = "editDesigner"
  override def viewTitle = "View Designer"
  override def listTitle = "List Designer"
  override def editTitle = "Edit Designer"
    
   private def bindToolsCSS(toolname : String, checkbox: NodeSeq) = 
	      ":checkbox" #>  checkbox &
	      "id=toolname" #>  toolname
    
  override def toForm(item : ItemType) : CssSel = {
//     		println("chaining asHtml from BaseRichEntitySnippet")
	     var userTools = item.tools.map(tool => tool.name.toString)         

         def toolSelected(toolName: String)(selected: Boolean) = {
           val tool = Tool.findByName(toolName).head
            if ( selected ) item.tools += tool
            else item.tools -= tool
          }
         
          def listTools(template: NodeSeq) : NodeSeq  =             
            item.tools.map(tool => tool.name.toString).
            	flatMap( toolName =>
           		bindToolsCSS(toolName, SHtml.checkbox(userTools.contains(toolName), value => toolSelected(toolName)(value) ))(template) 
           		)          

//              bindFieldsCSS(xhtml)
   (
       "#firstname" #> item.firstName .toForm &
       "#lastname" #> item.lastName  .toForm &       
       "#showimage" #> item.icon.asHtml &
       "#listtools" #> listTools _ 
   ) &
        (super.toForm(item))
   }
  
   //   abstract override
   override def asHtml(item : ItemType) : CssSel = {
//     		println("chaining asHtml from BaseRichEntitySnippet")
      
          def listTools(template: NodeSeq) : NodeSeq  =             
            item.tools.map(tool => tool.name.toString).
            	flatMap( toolName =>
           		bindToolsCSS(toolName, SHtml.checkbox(true, (_) => () ))(template) 
           		)                         
             //  )
   (
       "#title *" #> "%s %s".format(item.firstName, item.lastName ) &
       "#listtools" #> listTools _ 
   ) &
   (super.asHtml(item))
  }
//    override def asHtml(item : ItemType) : CssSel = {
//    		println("chaining asHtml from ProjectSnippet")
//    		
////    		println("finished cssselector: "+super.asHtml(item).toString)
//    		
//    		super.asHtml(item)
//  }

  override def localDispatch : DispatchIt = dispatchEditOwn orElse super.localDispatch
     
  def dispatchEditOwn : DispatchIt = {
     case "editOwn" => editOwn _
   }
   	      
  def editOwn(xhtml: NodeSeq) : NodeSeq  =  { 
    if (User.loggedIn_?)
	    toForm(User.currentUser.get)(xhtml) 
	 else 
	   LoginSnippet.notLoggedInMessage   
  }   
}