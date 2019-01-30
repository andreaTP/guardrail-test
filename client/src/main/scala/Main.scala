package codacy.petstore

object Client {

  def execute(): String = {
    import com.codacy.petstore.client.pets._

    import akka.actor.ActorSystem
    import akka.stream.ActorMaterializer
    import akka.http.scaladsl.Http
    import akka.http.scaladsl.model.HttpRequest
    import akka.http.scaladsl.model.HttpResponse
    import scala.util.Right
    import scala.concurrent._
    import scala.concurrent.duration._

    implicit val system = ActorSystem()
    implicit val mat = ActorMaterializer()
    implicit val ec = system.dispatcher
    implicit val http = Http(system)

    implicit val clientReqToResp: HttpRequest => Future[HttpResponse] = {
      req => http.singleRequest(req)
    }

    val client = PetsClient("http://localhost:8080")

    val petsList = Await.result(client.listPets().value, 5 seconds)

    petsList match {
      case Right(pl) =>
        system.terminate()
        pl.toString
      case _ => "error"
    }
  }

}

object Main extends App {
  println(Client.execute())
}