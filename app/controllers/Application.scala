package controllers

import play.api.Logger
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.WS
import play.api.libs.ws.WSRequestHolder
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.libs.json.JsObject
import play.api.libs.json.JsNumber
import play.api.libs.json.JsString

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  def walkScoreApi(url: String) = Action.async(parse.json) { implicit request =>
    request.body match {
      case JsObject(fields) => 
        val queryValues = fields.map { f => 
          (
              f._1, 
              f._2 match { 
                case s: JsString => s.as[String]
                case n: JsNumber => n.as[Double].toString()
                case _ => "" // Unknown value
              }
          )
        }
        
        val holder: WSRequestHolder = WS.url(url).withQueryString(queryValues: _*)
        
        Logger.debug("query strings: " + holder.queryString);
        
        holder.get().map( response => Ok(response.body) )
      case _ =>
        Logger.error("request.body error: " + request.body)
        scala.concurrent.Future { BadRequest("Expecting application/json request body") }
    }
  }
  
}
