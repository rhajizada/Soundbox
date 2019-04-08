package com.soundboxserver

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import io.ktor.features.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.get

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("spotify-link")
        header("apple-link")
        header("tidal-link")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    val client = HttpClient(Apache)

    suspend fun getContext(url: String): String{
        return client.get<String>(url)
    }

    routing {
        get("/") {
            call.respondText("hello", contentType = ContentType.Text.Html)
        }
        get("/spotify"){
            println("Spotify link ${call.request.header("spotify-link")}")
            call.respondText("Spotify link received\n", contentType = ContentType.Text.Plain)
            var songInfo = getSpotifySongInfo(getContext(call.request.header("spotify-link") as String))
            println("Song name: ${songInfo[0]}, Artist: ${songInfo[1]}, Album: ${songInfo[2]}" )
        }
        get("/apple"){
            println("Apple link ${call.request.header("apple-link")}")
            call.respondText("Apple music link received", contentType = ContentType.Text.Plain)
            var htmlAsString = getContext(call.request.header("apple-link") as String)
            var htmlList = htmlAsString.split('\n')
        }
        get("/tidal"){
            println("Tidal link ${call.request.header("tidal-link")}")
            call.respondText("Tidal link received", contentType = ContentType.Text.Plain)
            var htmlAsString = getContext(call.request.header("tidal-link") as String)
            var htmlList = htmlAsString.split('\n')
        }
    }
}

fun getSpotifySongInfo(x: String): List<String>{
    var htmlList = x.split('\n') // splits whole html by new line and puts it into html list
    var songInfo: MutableList<String> = mutableListOf<String>()
    songInfo.add(htmlList[2].split("<title>", "</title>")[1].split(", a song by ")[0]) // Adding song name
    songInfo.add(htmlList[2].split("<title>", "</title>")[1].split(", a song by ")[1].split(" on Spotify")[0]) // Adding Artist name
    songInfo.add(htmlList[40].split("</a></div></section></div>")[0].split(">")[1]) // Adding album name
    //println(htmlList[41])
    return songInfo
}




