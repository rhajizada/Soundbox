import kotlin.browser.document


fun main(args: Array<String>) {
    println("Hello")

    val x = "Generated using kotlin"
    var div = document.createElement("div")
    var h1 = document.createElement("h1")
    var b1 = document.createElement("button")
    val h2 = document.createElement("h2")
    div.appendChild(h1)
    div.appendChild(b1)
    div.appendChild(h2)
    b1.textContent = "Click Me"
    h1.textContent = "I am generated using Kotlin's DOM API"
    b1.addEventListener("click", { h2.textContent = "You clicked the button"})
    val root = document.getElementById("root")
    root!!.appendChild(div)

}