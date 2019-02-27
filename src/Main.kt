import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import kotlin.browser.document


fun main(args: Array<String>) {
    println("Hello")
    val main_card = document.getElementById("main_card") as HTMLDivElement
    val spotify_input = document.getElementById("spotify_input") as HTMLInputElement
    val apple_input = document.getElementById("apple_input") as HTMLInputElement
    val tidal_input = document.getElementById("tidal_input") as HTMLInputElement
    //tidal_input!!.placeholder = "I changed this using kotlin"

    val submitBtn = document.createElement("button") as HTMLButtonElement
    submitBtn.className = "btn btn-primary"
    submitBtn.type = "submit"
    submitBtn.innerText = "Submit"
    main_card.appendChild(submitBtn)
    fun printInputs(){
        println("Spotify: ${spotify_input.value}")
        println("Apple Music: ${apple_input.value}")
        println("Tidal: ${tidal_input.value}")
    }

    submitBtn.addEventListener("click", { printInputs()})

//    val x = "Generated using kotlin"
//    var div = document.createElement("div")
//    var h1 = document.createElement("h1")
//    var b1 = document.createElement("button")
//    val h2 = document.createElement("h2")
//    div.appendChild(h1)
//    div.appendChild(b1)
//    div.appendChild(h2)
//    val root = document.getElementById("root")
//    b1.textContent = "Click Me"
//    h1.textContent = "I am generated using Kotlin's DOM API"
//    b1.addEventListener("click", { println("$x")})
//
//    root!!.appendChild(div)

}
