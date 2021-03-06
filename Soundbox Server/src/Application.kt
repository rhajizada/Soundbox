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
    suspend fun getJSON(url: String): JSONResponse{
        val response = client.get<String>(url)
        if(response.isNullOrEmpty()){
            println("Cant get response from $url")
        }
        val gson = GsonBuilder().create()
        val responseJSON = gson.fromJson(response, JSONResponse::class.java)
        return responseJSON
    } // Makes get request and returns JSON as Response object

    suspend fun search(song: SongInfo):SongData {
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
        var albumArt: String = ""

        if(spotifySongLink.isNullOrEmpty()){
            albumArt = (getContext(appleSongLink)).split("<source class=\"we-artwork__source\"")[1].split("<style>")[0].split(" 1x")[0].split("srcset=\"")[1]
        }
        else{
            albumArt ="http://" + getContext(spotifySongLink).split("\n")[33].split("style=\"background-image:url(//")[1].split("),")[0]
        }
        val Response = SongData(song, albumArt, spotifySongLink, appleSongLink, tidalSongLink, deezerSongLink)
        println(Response.toString())
        return Response
    }

    routing {
        get("/song"){
            try{
                val platform = call.request.header("platform") as String
                val link = (call.request.header("link") as String).fixLink()
                val songInfo = SongInfo(platform, getContext(link))
                call.respondText(search(songInfo).toString(), contentType = ContentType.Text.Plain)
            }
            catch(e: java.lang.IndexOutOfBoundsException){
                call.respond(HttpStatusCode(401, "Index out of Bond"), "Problem with link")
            }
            catch(e: java.net.ConnectException){
                call.respond(HttpStatusCode(402, "Wrong formatted Link"), "Link formatted wrong")
            }
        }
//        get("/song-card"){
//            val platform = call.request.header("platform") as String
//            val link = (call.request.header("link") as String).fixLink()
//            val songInfo = SongInfo(platform, getContext(link))
//            val songData = search(songInfo)
//            call.respondHtml {
//                head{
//                    title="Soundbox"
//                    link("https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css", "stylesheet")
//                    script("javascript", "https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"){}
//                }
//                body{
//                    div("row"){
//                        div("col s12 m7"){
//                            div("card small"){
//                                div("card-image"){
//                                    img("Album Artwork", songData.artwork, "")
//                                    span("card-title"){
//                                        +songInfo.song
//                                        br
//                                        +songInfo.artist
//                                        br
//                                        +songInfo.album
//                                    }
//                                }
//                                div("card-content"){
//                                }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }
    }

fun String.getRidOfWrong(): String =  this.replace("&#039;", "'").replace("&amp;", "&").replace("&quot;", "\"")
fun String.sanitize(): String =  this.replace("#", "%23").replace(" ", "+")
fun String.fixLink(): String = this.replace("https", "http")





