package cpp.scraper.milanuncios.infrastructure.repository.ad

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "ads")
data class AdEntity(
    val id: Long,
    val categories: List<Category>,
    val category: Category,
    val categoryPath: List<CategoryPathItem>,
    val sellerType: SellerType,
    val author: Author,
    val title: String,
    val description: String,
    val isReserved: Boolean,
    val contactMethods: ContactMethods,
    val price: Price,
    val location: Location,
    val sellType: String,
    val origin: Origin,
    val images: List<String>,
    val breadcrumbs: Breadcrumbs,
    val url: String,
    val attributes: List<Attribute>,
    val legalAttributes: List<Attribute>,
    val extras: List<String>,
    val sortDate: String,
    val publicationDate: String,
    val stats: Stats,
    val updateDate: String
) {
    data class Category(
        val id: Int,
        val name: String,
        val slug: String
    )

    data class CategoryPathItem(
        val id: Int
    )

    data class SellerType(
        val value: String,
        val isPrivate: Boolean
    )

    data class Author(
        val id: String,
        val userName: String,
        val isEmailVerified: Boolean,
        val location: Location,
        val isCompleted: Boolean
    )

    data class ContactMethods(
        val chat: Boolean,
        val phone: Boolean,
        val form: Boolean
    )

    data class Price(
        val cashPrice: CashPrice,
        val financedPrice: FinancedPrice
    )

    data class CashPrice(
        val value: Int,
        val includeTaxes: Boolean
    )

    data class FinancedPrice(
        val value: Int
    )

    data class Location(
        val province: Province,
        val city: City
    )

    data class Province(
        val id: Int,
        val name: String,
        val slug: String
    )

    data class City(
        val id: Int,
        val name: String,
        val slug: String
    )

    data class Origin(
        val name: String,
        val provider: String
    )

    data class Breadcrumbs(
        val urls: List<Url>,
        val scriptTagType: String,
        val breadcrumbJsonList: BreadcrumbJsonList
    )

    data class Url(
        val url: String,
        val label: String
    )

    data class BreadcrumbJsonList(
        val context: String,
        val type: String,
        val itemListElement: List<ListItem>
    )

    data class ListItem(
        val type: String,
        val position: Int,
        val name: String,
        val item: String
    )

    data class Attribute(
        val type: String,
        val fieldFormatted: String,
        val value: String,
        val valueFormatted: String
    )

    data class Stats(
        val listings: Int,
        val favorites: Int
    )
}


