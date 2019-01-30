package example.myapp.helloworld

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.ConfigFactory

import scala.concurrent.{ExecutionContext, Future}

import com.codacy.petstore.server.definitions._
import com.codacy.petstore.server.pets._

object Server {

  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.defaultApplication()
    val system = ActorSystem("HelloWorld", conf)
    new Server(system).run()
  }
}

class Server(system: ActorSystem) {

  // Akka boot up code
  implicit lazy val sys: ActorSystem = system
  implicit lazy val mat: Materializer = ActorMaterializer()
  implicit lazy val ec: ExecutionContext = sys.dispatcher

  val petStore = new PetsHandler {
    def createPets(respond: PetsResource.createPetsResponse.type)(): Future[PetsResource.createPetsResponse] = {
      Future.successful(
        PetsResource.createPetsResponse.Created
      )
    }
    def listPets(respond: PetsResource.listPetsResponse.type)(limit: Option[Int] = None): Future[PetsResource.listPetsResponse] = {
      Future.successful(
        PetsResource.listPetsResponse.OK(
          Vector(Pet(1L, "Guido"))
        )
      )
    }
    def showPetById(respond: PetsResource.showPetByIdResponse.type)(petId: String): Future[PetsResource.showPetByIdResponse] = {
      Future.successful(
        PetsResource.showPetByIdResponse.OK(
          Vector(Pet(petId.toLong, "Guido"))
        )
      )
    }
  }

  def run(): Future[Http.ServerBinding] = {
    val bound = Http().bindAndHandle(
      PetsResource.routes(petStore),
      interface = "127.0.0.1",
      port = 8080)

    bound.foreach { binding =>
      println(s"Server bound to: ${binding.localAddress}")
    }

    bound
  }

}