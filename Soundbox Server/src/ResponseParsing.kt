package com.soundboxserver

class Response(val kind: String,val url: URL,val queries: Queries,val items: List<Item>){
    fun printItems(){
        for(i in items){
            i.print()
        }
    }
    fun getSpotifyLink(): String{
        var r: String = ""
        for(i in items.asReversed()){
            if(i.link.contains("open.spotify.com/album") || i.link.contains("open.spotify.com/track")){
                r = i.link
            }
        }
        return r
    }
    fun getAppleLink(): String{
        var r: String = ""
        for(i in items){
            if(i.link.contains("https://itunes.apple.com/us/album/") || i.link.contains("https://itunes.apple.com/us/track/")){
                r = i.link
            }
        }
        return r
    }
    fun getTidalLink(): String{
        var r: String = ""
        for(i in items){
            if(i.link.contains("https://tidal.com/browse/album/") || i.link.contains("https://tidal.com/browse/track/")){
                r = i.link
            }
        }
        return r
    }
}


class URL(val type: String,val template: String)
class Queries(val requests: List<Request>,val nextPages: List<NextPage>)
class Request(val title: String,val totalResults: String,val searchTerms: String,val count: Int,val startIndex: Int,val inputEncoding: String,val outputEncoding: String,val safe: String,val cx: String)
class NextPage(val title: String,val totalResults: String,val searchTerms: String,val count: Int,val startIndex: Int,val inputEncoding: String,val outputEncoding: String,val safe: String,val cx: String)
class Context(val title: String)
class SearchInformation(val searchTime: Double,val formattedSearchTime: String,val totalResults: String,val formattedTotalResults: String)
class Item(val kind: String,val title: String,val htmlTitle: String,val link: String,val displayLink: String,val snippet: String,val htmlSnippet: String,val cache_id: String,val formattedUrl: String,val htmlFormattedUrl: String){
    fun print(){
        println("Kind: $kind\nTitle: $title\nhtmlTitle: ${htmlTitle}\nlink: $link\ndisplayLink: $displayLink\nsnippet: $snippet\nhtmlSnippet: ${htmlSnippet}\ncache_id: $cache_id\nformattedUrl: $formattedUrl\nhtmlFormattedUrl: $htmlFormattedUrl")
    }
}