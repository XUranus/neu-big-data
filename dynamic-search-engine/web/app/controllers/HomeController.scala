package controllers

import javax.inject._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a search message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Welcome"))
  }

  def search(keyword:String) = Action {
    val res = HbaseQuery.searchKeyWord(keyword).
      map(pair=>(pair._1,limitStr(HbaseQuery.getArticle(pair._1)),pair._2))
    //(index,content,count)
    Ok(views.html.search(res))
  }

  def detail(index:String) = Action {
    val detail = HbaseQuery.getArticle(index)
    Ok(views.html.detail(detail))
  }

  def limitStr(s:String): String = s match {
      case _s if _s.length > 300 => s.substring(0,300)+"..."
      case _ => s
  }


}
