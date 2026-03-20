package projects.kankan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KankanServiceApplication
fun main(args: Array<String>) {
    runApplication<KankanServiceApplication>(*args)
}