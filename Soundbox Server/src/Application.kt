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
import io.ktor.client.call.call
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.request.get

val API_KEY = "" // Put your google custom search api key here
val CX = "" // Put your google custom seasrch cx code here
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
        header("link")
        header("platform")
        header("spotify-link")
        header("apple-link")
        header("tidal-link")
        header("deezer-link")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    val client = HttpClient(Apache) {
        expectSuccess = false
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    suspend fun getContext(url: String): String{
        var response = client.get<String>(url)
        if(response.isNullOrEmpty()){
            println("Cant get response from $url")
        }
        return response
    } // Makes simple get request and returns html od site as string
    suspend fun getJSON(url: String): Response{
        val response = client.get<String>(url)
        if(response.isNullOrEmpty()){
            println("Cant get response from $url")
        }
        val gson = GsonBuilder().create()
        val responseJSON = gson.fromJson(response, Response::class.java)
        return responseJSON
    } // Makes get request and returns JSON as Response object

    suspend fun search(song: Song):String {
        val spotifyAPISeach = "https://www.googleapis.com/customsearch/v1?q=spotify+${song.song.sanitize()}+${song.artist.sanitize()}+${song.album.sanitize()}&cx=${CX}&key=${API_KEY}"
        val appleAPISearch = "https://www.googleapis.com/customsearch/v1?q=apple+music+${song.song.sanitize()}+${song.artist.sanitize()}+${song.album.sanitize()}&cx=${CX}&key=${API_KEY}"
        val tidalAPISearch = "https://www.googleapis.com/customsearch/v1?q=tidal+${song.song.sanitize()}+${song.artist.sanitize()}+${song.album.sanitize()}&cx=${CX}&key=${API_KEY}"
        val deezerAPISearch = "https://www.googleapis.com/customsearch/v1?q=deezer+${song.song.sanitize()}+${song.artist.sanitize()}+${song.album.sanitize()}&cx=${CX}&key=${API_KEY}"
        var spotifyResponse = getJSON(spotifyAPISeach)
        var appleResponse = getJSON(appleAPISearch)
        var tidalResponse = getJSON(tidalAPISearch)
        var deezerResponse = getJSON(deezerAPISearch)

        val spotifySongLink = spotifyResponse.getSpotifyLink(song)
        val appleSongLink = appleResponse.getAppleLink(song)
        val tidalSongLink = tidalResponse.getTidalLink(song)
        val deezerSongLink = deezerResponse.getDeezerLink(song)
        println("Spotify: $spotifySongLink\nApple Music: $appleSongLink\nTidal: $tidalSongLink\nDeezer: $deezerSongLink")
        var albumArt: String = ""

        if(spotifySongLink.isNullOrEmpty()){
            albumArt = (getContext(appleSongLink)).split("<source class=\"we-artwork__source\"")[1].split("<style>")[0].split(" 1x")[0].split("srcset=\"")[1]
        }
        else{
            albumArt ="http://" + getContext(spotifySongLink).split("\n")[33].split("style=\"background-image:url(//")[1].split("),")[0]
        }
        val response = "Song: ${song.song}\nArtist: ${song.artist}\nAlbum: ${song.album}\nArtwork: ${albumArt}\nSpotify: ${spotifySongLink}\nApple: $appleSongLink\nTidal: $tidalSongLink\nDeezer: $deezerSongLink"
        return response
    }

    routing {
        get("/song"){
            val platform = call.request.header("platform") as String
            val link = (call.request.header("link") as String).fixLink()
            val songInfo = Song(platform, getContext(link))
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
fun String.fixLink(): String = this.replace("https", "http")





