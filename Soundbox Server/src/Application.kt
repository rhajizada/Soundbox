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


val API_KEY = "AIzaSyCFrgO-Q52LzCjdz09GMTkLEI-BGb1DWoA"
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

    val client = HttpClient(Apache) {

    }

    suspend fun getContext(url: String): String{
        println("GETTING $url")
        return client.get<String>(url)
    }
    suspend fun getJSON(url: String){
        val response = client.get<String>(url)
        println(response.split("\n")[48])
    }

    suspend fun search(x: List<String>):String {
        val spotifySearchLink = "https://www.google.com/search?q=spotify ${x[0].sanitize()}+${x[1].sanitize()}+${x[2].sanitize()}".replace(' ', '+')
        val appleSearchLink = "https://www.google.com/search?q=apple music ${x[0].sanitize()}+${x[1].sanitize()}+${x[2].sanitize()}".replace(' ', '+')
        val tidalSearchLink = "https://www.google.com/search?q=tidal ${x[0].sanitize()}+${x[1].sanitize()}+${x[2].sanitize()}".replace(' ', '+')

        val spotifyAPISeach = "https://www.googleapis.com/customsearch/v1?q=spotify+${x[0].sanitize()}+${x[1].sanitize()}+${x[2].sanitize()}&cx=008255740316595556921%3Ap6hob4sk9xk&key=${API_KEY}"

        var spotifySongLink = getContext(spotifySearchLink).split('\n')[1].split("<ol><div class=")[1].split("&amp")[0].split("/url?q=")[1]
        var appleSongLink = getContext(appleSearchLink).split('\n')[1].split("<ol><div class=")[1].split("&amp")[0].split("/url?q=")[1]
        var tidalSongLink = getContext(tidalSearchLink).split('\n')[1].split("<ol><div class=")[1].split("&amp")[0].split("/url?q=")[1]
        var albumArt = (getContext(appleSongLink)).split("<source class=\"we-artwork__source\"")[1].split("<style>")[0].split(" 1x")[0].split("srcset=\"")[1]

        getJSON(spotifyAPISeach)

        if(albumArt.isNullOrBlank()){
            albumArt = " "
        }
        val response = "Song: ${x[0]}\nArtist: ${x[1]}\nAlbum: ${x[2]}\nArtwork: ${albumArt}\nSpotify: ${spotifySongLink}\nApple: $appleSongLink\nTidal: $tidalSongLink"
        return response
    }

    routing {
        get("/") {
            call.respondText("hello", contentType = ContentType.Text.Html)
        }
        get("/spotify"){
            println("Spotify link ${call.request.header("spotify-link")}")
            var songInfo = getSpotifySongInfo(getContext(call.request.header("spotify-link") as String))
            println(songInfo[0].replace("&#039;", "'"))
            println("Song name: ${songInfo[0]}, Artist: ${songInfo[1]}, Album: ${songInfo[2]}" )
            println(search(songInfo))
            call.respondText(search(songInfo), contentType = ContentType.Text.Plain)
        }
        get("/apple"){
            println("Apple link ${call.request.header("apple-link")}")
            var songInfo = getAppleSongInfo(getContext(call.request.header("apple-link") as String))

            println("Song name: ${songInfo[0]}, Artist: ${songInfo[1]}, Album: ${songInfo[2]}" )
            println(search(songInfo))
            call.respondText(search(songInfo), contentType = ContentType.Text.Plain)
        }
        get("/tidal"){
            println("Tidal link ${call.request.header("tidal-link")}")
            var songInfo = getTidalSongInfo(getContext(call.request.header("tidal-link") as String))
            println("Song name: ${songInfo[0]}, Artist: ${songInfo[1]}, Album: ${songInfo[2]}" )
            println(search(songInfo))
            call.respondText(search(songInfo), contentType = ContentType.Text.Plain)
        }
    }

}

fun getSpotifySongInfo(x: String): MutableList<String>{
    var htmlList = x.split('\n') // splits whole html by new line and puts it into html list
    var songInfo: MutableList<String> = mutableListOf<String>()
    songInfo.add(htmlList[2].split("<title>", "</title>")[1].split(", a song by ")[0].getRidOfWrong()) // Adding song name
    songInfo.add(htmlList[2].split("<title>", "</title>")[1].split(", a song by ")[1].split(" on Spotify")[0].getRidOfWrong()) // Adding Artist name
    songInfo.add(htmlList[40].split("</a></div></section></div>")[0].split(">")[1].getRidOfWrong()) // Adding album name
    //println(htmlList[41])
    return songInfo
}

fun getAppleSongInfo(x: String): MutableList<String>{
    var htmlList = x.split('\n')
    var songInfo: MutableList<String> = mutableListOf<String>()
    songInfo.add(htmlList[18].split("content=")[1].split(" by")[0].removeRange(0,1).getRidOfWrong()) // Song name works
    songInfo.add(htmlList[18].split("by ")[1].split('"')[0].getRidOfWrong()) // Artist name
    songInfo.add(htmlList[14].split("listen, ")[1].split(", ${songInfo[1]}")[0].getRidOfWrong()) // Album name testing
    return  songInfo
}

fun getTidalSongInfo(x: String): MutableList<String>{
    var htmlList =x.split('\n')
    var songInfo: MutableList<String> = mutableListOf<String>()
    songInfo.add(htmlList[0].split("name")[18].removeRange(0..2).split("description")[0].reversed().removeRange(0..2).reversed().getRidOfWrong()) //Song name
    songInfo.add(htmlList[0].split("name")[20].split("artist-list-link hover-desktop")[1].split("</a>")[0].removeRange(0..1).getRidOfWrong()) //Artist name
    songInfo.add(htmlList[0].split("name")[20].split("calc(33.33vw - 1.5rem), calc(100vw - 3rem)")[1].split(" class=")[0].removeRange(0..6).reversed().removeRange(0..0).reversed().getRidOfWrong()) //Abum name
    return songInfo
}
fun String.getRidOfWrong(): String =  this.replace("&#039;", "'").replace("&amp;", "&").replace("&quot;", "\"")
fun String.sanitize(): String =  this.replace("#", "%23")




