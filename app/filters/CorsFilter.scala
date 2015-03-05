package filters

import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * @see http://windrush.io/tech/2013/12/17/cors-and-play.html
 * @see http://stackoverflow.com/questions/19307850/writing-custom-filters-for-play-2-2-in-java/19310057#19310057
 */
object CorsFilter extends Filter {
  def apply(f: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {
    val result = f(rh)
    result.map(_.withHeaders(
        "Access-Control-Allow-Headers" -> "Origin, X-Requested-With, Content-Type, Accept",
        "Access-Control-Allow-Methods" -> "GET, OPTIONS, POST, PUT, DELETE",
        "Access-Control-Allow-Origin" -> "*"
    ))
  }
}