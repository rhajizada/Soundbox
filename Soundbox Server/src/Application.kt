package com.soundboxserver

import com.google.gson.GsonBuilder
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
import io.ktor.client.features.json.*
import io.ktor.client.request.get

val API_KEY = "" // Put your google custom search api key here
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
        header("deezer-link")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    suspend fun getContext(url: String): String{
        return client.get<String>(url)
    } // Makes simple get request and returns html od site as string
    suspend fun getJSON(url: String): Response{
        val response = client.get<String>(url)
        val gson = GsonBuilder().create()
        val responseJSON = gson.fromJson(response, Response::class.java)
        return responseJSON
    } // Makes get request and returns JSON as Response object

    suspend fun search(song: Song):String {
        val spotifyAPISeach = "https://www.googleapis.com/customsearch/v1?q=spotify+${song.song.sanitize()}+${song.artist.sanitize()}+${song.album.sanitize()}&cx=008255740316595556921%3Ap6hob4sk9xk&key=${API_KEY}"
        val appleAPISearch = "https://www.googleapis.com/customsearch/v1?q=apple+music+${song.song.sanitize()}+${song.artist.sanitize()}+${song.album.sanitize()}&cx=008255740316595556921%3Ap6hob4sk9xk&key=${API_KEY}"
        val tidalAPISearch = "https://www.googleapis.com/customsearch/v1?q=tidal+${song.song.sanitize()}+${song.artist.sanitize()}+${song.album.sanitize()}&cx=008255740316595556921%3Ap6hob4sk9xk&key=${API_KEY}"
        println(tidalAPISearch)
        var spotifyResponse = getJSON(spotifyAPISeach)
        var appleResponse = getJSON(appleAPISearch)
        var tidalResponse = getJSON(tidalAPISearch)

        val spotifySongLink = spotifyResponse.getSpotifyLink(song)
        val appleSongLink = appleResponse.getAppleLink(song)
        val tidalSongLink = tidalResponse.getTidalLink(song)
        var albumArt = (getContext(appleSongLink)).split("<source class=\"we-artwork__source\"")[1].split("<style>")[0].split(" 1x")[0].split("srcset=\"")[1]


        if(albumArt.isNullOrBlank()){
            albumArt = " "
        }
        val response = "Song: ${song.song}\nArtist: ${song.artist}\nAlbum: ${song.album}\nArtwork: ${albumArt}\nSpotify: ${spotifySongLink}\nApple: $appleSongLink\nTidal: $tidalSongLink"
        return response
    }

    routing {
        get("/") {
            call.respondText("hello", contentType = ContentType.Text.Html)
        }
        get("/spotify"){
            println("Spotify link ${call.request.header("spotify-link")}")
            var songInfo = getSpotifySongInfo(getContext(call.request.header("spotify-link") as String))
            songInfo.print()
            call.respondText(search(songInfo), contentType = ContentType.Text.Plain)
        }
        get("/apple"){
            println("Apple link ${call.request.header("apple-link")}")
            var songInfo = getAppleSongInfo(getContext(call.request.header("apple-link") as String))
            songInfo.print()
            call.respondText(search(songInfo), contentType = ContentType.Text.Plain)
        }
        get("/tidal"){
            println("Tidal link ${call.request.header("tidal-link")}")
            var songInfo = getTidalSongInfo(getContext(call.request.header("tidal-link") as String))
            songInfo.print()
            call.respondText(search(songInfo), contentType = ContentType.Text.Plain)
        }
        get("/deezer"){
            println("Deezer link ${call.request.header("deezer-link")}")
            var songInfo = getDeezerSongInfo(getContext(call.request.header("deezer-link") as String))
            call.respondText(search(songInfo), contentType = ContentType.Text.Plain)
        }
    }

}

fun getSpotifySongInfo(x: String): Song{
    return Song("spotify", x)
}

fun getAppleSongInfo(x: String): Song{
    return Song("apple", x)
}

fun getTidalSongInfo(x: String): Song{
   return Song("tidal", x)
}

fun getDeezerSongInfo(x: String): Song{
    return Song("deezer", x)
}

fun String.getRidOfWrong(): String =  this.replace("&#039;", "'").replace("&amp;", "&").replace("&quot;", "\"")
fun String.sanitize(): String =  this.replace("#", "%23").replace(" ", "+")





