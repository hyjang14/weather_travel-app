package ddwu.com.mobileapp.week12.TravelProject.data.network

data class Root(
    val response: Response,
)

data class Response(
    val header: Header,
    val body: Body,
)

data class Header(
    val resultCode: String,
    val resultMsg: String,
)

data class Body(
    val items: Items,
    val numOfRows: Long,
    val pageNo: Long,
    val totalCount: Long,
)

data class Items(
    val item: List<Item>,
)

data class Item(
    val addr1: String,
    val addr2: String,
    val areacode: String,
    val booktour: String,
    val cat1: String,
    val cat2: String,
    val cat3: String,
    val contentid: String,
    val contenttypeid: String,
    val createdtime: String,
    val dist: String,
    val firstimage: String,
    val firstimage2: String,
    val cpyrhtDivCd: String,
    val mapx: String,
    val mapy: String,
    val mlevel: String,
    val modifiedtime: String,
    val sigungucode: String,
    val tel: String,
    val title: String,
)
