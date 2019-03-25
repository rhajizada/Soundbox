import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import kotlin.browser.*

external class XMLHttpRequest
var xhttp :dynamic= XMLHttpRequest(); // Adding native JavaScript library for http calls
val main_card = document.getElementById("main_card") as HTMLDivElement
val spotify_input = document.getElementById("spotify_input") as HTMLInputElement
val apple_input = document.getElementById("apple_input") as HTMLInputElement
val tidal_input = document.getElementById("tidal_input") as HTMLInputElement

fun main(args: Array<String>) {

    //tidal_input!!.placeholder = "I changed this using kotlin"
    val submitBtn = document.createElement("button") as HTMLButtonElement
    submitBtn.className = "btn btn-primary"
    submitBtn.type = "submit"
    submitBtn.innerText = "Submit"

    var requestBtn = document.createElement("button") as HTMLButtonElement
    requestBtn.className = "btn btn-primary"
    requestBtn.type = "submit"
    requestBtn.innerText = "Request sample"

    main_card.appendChild(submitBtn)
    main_card.appendChild(requestBtn)
    submitBtn.addEventListener("click", { printInputs()})
    requestBtn.addEventListener("click", {getAsync("https://httpbin.org/get"){response -> print(response)}})
}

fun printInputs(){
    println("Spotify: ${spotify_input.value}")
    println("Apple Music: ${apple_input.value}")
    println("Tidal: ${tidal_input.value}")
}

fun getRequest(url: String): String{
    var response: String = " "
    xhttp.open("GET", "https://crossorigin.me/" + url, true);

    xhttp.onreadystatechange=fun(){
        println(xhttp.readyState)
        println(xhttp.status)
        response =  xhttp.responseText as String
    }
    xhttp.send();
    return response
}

private fun getAsync(url: String, callback: (String) -> Unit) {
    val xmlHttp: dynamic = XMLHttpRequest()
    if(xmlHttp) {
        xmlHttp.open("GET", "https://crossorigin.me/" + url) // Allows us easily bypass CORS
        xmlHttp.withCredentials = true
        xmlHttp.onload = {
            if (xmlHttp.readyState == 4.toShort() && xmlHttp.status == 200.toShort()) {
                callback.invoke(xmlHttp.responseText)
            }
        }
        xmlHttp.send()
    }

}

