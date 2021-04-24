import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

var lastId = 0
val articles = mutableListOf<Article>()
val pageCount = 10

fun addArticle(title : String, body : String) : Int{
    val id = lastId + 1
    val regDate = getDateNowStr()
    val updateDate = getDateNowStr()

    val article = Article(id, title, body, regDate, updateDate)
    articles.add(article)
    lastId = id
    return id
}

fun makeTestArticle(){
    for(i in 1..50){
        val title = "제목$i"
        val body = "내용$i"
        addArticle(title, body)
    }
}

fun getArticleById(id : Int) : Article?{
    for(article in articles){
        if(article.id == id){
            return article
            continue
        }
    }
    return null
}

fun articleFilter(page : Int, keyword : String) : List<Article>{

    var articles1 = articles
    if(keyword.isNotEmpty()){
        articles1 = mutableListOf()
        for(article in articles){
            if(article.title.contains(keyword)){
                articles1.add(article)
            }
        }
    }


    val startIndex = articles1.lastIndex - ((page - 1) * 10)
    var endIndex = startIndex - pageCount + 1

    if(endIndex < 0){
        endIndex = 0
    }
    var articles2 = mutableListOf<Article>()
    for(i in startIndex downTo endIndex){
        articles2.add(articles1[i])
    }
    return articles2
}

fun main(){
    println("프로그램 시작")
    makeTestArticle()

    while(true) {
        print("명령어 입력 : ")
        val cmd = readLine()!!.trim()

        if(cmd == "exit"){
            break
        }
        if(cmd == "article write"){

            print("제목 입력 : ")
            val title = readLine()!!.trim()
            print("내용 입력 : ")
            val body = readLine()!!.trim()
            val id = addArticle(title, body)
            println("$id 번 게시물이 등록되었습니다.")
        }
        else if(cmd.startsWith("article list ")){
            val inputInformation = cmd.trim().split(" ")
            var page = 0
            var keyword = ""
            if(inputInformation.size == 3){
                page = inputInformation[2].toInt()
            }
            else if(inputInformation.size == 4){
                keyword = inputInformation[2]
                page = inputInformation[3].toInt()
            }

            val filteredArticles = articleFilter(page, keyword)
            for(article in filteredArticles){
                println("번호 : ${article.id} / 등록날짜 : ${article.regDate} / 제목 : ${article.title} / ")
            }

        }
        else if(cmd.startsWith("article detail ")){
            val id = cmd.trim().split(" ")[2].toInt()

            val article = getArticleById(id)
            if(article == null){
                println("없는 게시물 번호입니다.")
                continue
            }
            println("번호 : ${article.id}")
            println("제목 : ${article.title}")
            println("내용 : ${article.body}")
            println("등록날짜 : ${article.regDate}")
            println("수정날짜 : ${article.updateDate}")
        }
        else if(cmd.startsWith("article delete ")){
            val id = cmd.trim().split(" ")[2].toInt()

            val article = getArticleById(id)
            if(article == null){
                println("없는 게시물 번호입니다.")
                continue
            }
            articles.remove(article)
            println("$id 번 게시물 삭제 완료")
        }
        else if(cmd.startsWith("article modify ")){
            val id = cmd.trim().split(" ")[2].toInt()

            val article = getArticleById(id)
            if(article == null){
                println("없는 게시물 번호입니다.")
                continue
            }
            print("새 제목 : ")
            val title = readLine()!!.trim()
            print("새 내용 : ")
            val body = readLine()!!.trim()

            article.title = title
            article.body = body
            article.updateDate = getDateNowStr()
            println("$id 번 게시물 수정 완료")
        }

    }

    println("프로그램 끝")
}

data class Article(
    val id : Int,
    var title : String,
    var body : String,
    val regDate : String,
    var updateDate : String
){

}

fun getDateNowStr() : String{
    var now = LocalDateTime.now()
    var getNowStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초"))
    return getNowStr
}