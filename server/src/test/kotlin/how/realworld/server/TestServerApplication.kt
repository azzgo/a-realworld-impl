package how.realworld.server

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.with

@TestConfiguration(proxyBeanMethods = false)
class TestServerApplication

fun main(args: Array<String>) {
	fromApplication<ServerApplication>().with(TestServerApplication::class).run(*args)
}
