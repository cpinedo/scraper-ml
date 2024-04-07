package cpp.scraper.milanuncios.infrastructure.repository.ad

import org.json.JSONArray
import org.json.JSONObject

class AdEntityBuilder {
    fun fromJson(jsonObject: JSONObject): AdEntity {
        return AdEntity(
            id = jsonObject.getLong("id"),
            categories = parseCategories(jsonObject.getJSONArray("categories")),
            category = parseCategory(jsonObject.getJSONObject("category")),
            categoryPath = parseCategoryPath(jsonObject.getJSONArray("categoryPath")),
            sellerType = parseSellerType(jsonObject.getJSONObject("sellerType")),
            author = parseAuthor(jsonObject.getJSONObject("author")),
            title = jsonObject.getString("title"),
            description = jsonObject.getString("description"),
            isReserved = jsonObject.getBoolean("isReserved"),
            contactMethods = parseContactMethods(jsonObject.getJSONObject("contactMethods")),
            price = parsePrice(jsonObject.getJSONObject("price")),
            location = parseLocation(jsonObject.getJSONObject("location")),
            sellType = jsonObject.getString("sellType"),
            origin = parseOrigin(jsonObject.getJSONObject("origin")),
            images = parseImages(jsonObject.getJSONArray("images")),
            breadcrumbs = parseBreadcrumbs(jsonObject.getJSONObject("breadcrumbs")),
            url = jsonObject.getString("url"),
            attributes = parseAttributes(jsonObject.getJSONArray("attributes")),
            legalAttributes = parseAttributes(jsonObject.getJSONArray("legalAttributes")),
            extras = parseExtras(jsonObject.getJSONArray("extras")),
            sortDate = jsonObject.getString("sortDate"),
            publicationDate = jsonObject.getString("publicationDate"),
            stats = parseStats(jsonObject.getJSONObject("stats")),
            updateDate = jsonObject.getString("updateDate")
        )
    }

    private fun parseCategories(jsonArray: JSONArray): List<AdEntity.Category> {
        val categories = mutableListOf<AdEntity.Category>()
        for (i in 0 until jsonArray.length()) {
            val categoryObject = jsonArray.getJSONObject(i)
            val category = AdEntity.Category(
                id = categoryObject.getInt("id"),
                name = categoryObject.getString("name"),
                slug = categoryObject.getString("slug")
            )
            categories.add(category)
        }
        return categories
    }

    private fun parseCategory(jsonObject: JSONObject): AdEntity.Category {
        return AdEntity.Category(
            id = jsonObject.getInt("id"),
            name = jsonObject.getString("name"),
            slug = jsonObject.getString("slug")
        )
    }

    private fun parseCategoryPath(jsonArray: JSONArray): List<AdEntity.CategoryPathItem> {
        val categoryPath = mutableListOf<AdEntity.CategoryPathItem>()
        for (i in 0 until jsonArray.length()) {
            val categoryPathItemObject = jsonArray.getJSONObject(i)
            val categoryPathItem = AdEntity.CategoryPathItem(
                id = categoryPathItemObject.getInt("id")
            )
            categoryPath.add(categoryPathItem)
        }
        return categoryPath
    }

    private fun parseSellerType(jsonObject: JSONObject): AdEntity.SellerType {
        return AdEntity.SellerType(
            value = jsonObject.getString("value"),
            isPrivate = jsonObject.getBoolean("isPrivate")
        )
    }

    private fun parseAuthor(jsonObject: JSONObject): AdEntity.Author {
        val locationObject = jsonObject.getJSONObject("location")
        return AdEntity.Author(
            id = jsonObject.getString("id"),
            userName = jsonObject.getString("userName"),
            isEmailVerified = jsonObject.getBoolean("isEmailVerified"),
            location = AdEntity.Location(
                city = AdEntity.City(
                    locationObject.getJSONObject("city").getInt("id"),
                    locationObject.getJSONObject("city").getString("name"),
                    locationObject.getJSONObject("city").getString("slug"),
                ),
                province = AdEntity.Province(
                    locationObject.getJSONObject("province").getInt("id"),
                    locationObject.getJSONObject("province").getString("name"),
                    locationObject.getJSONObject("province").getString("slug"),
                )
            ),
            isCompleted = jsonObject.getBoolean("isCompleted")
        )
    }

    private fun parseContactMethods(jsonObject: JSONObject): AdEntity.ContactMethods {
        return AdEntity.ContactMethods(
            chat = jsonObject.getBoolean("chat"),
            phone = jsonObject.getBoolean("phone"),
            form = jsonObject.getBoolean("form")
        )
    }

    private fun parsePrice(jsonObject: JSONObject): AdEntity.Price {
        return AdEntity.Price(
            cashPrice = parseCashPrice(jsonObject.getJSONObject("cashPrice")),
            financedPrice = parseFinancedPrice(jsonObject.getJSONObject("financedPrice"))
        )
    }

    private fun parseCashPrice(jsonObject: JSONObject): AdEntity.CashPrice {
        return AdEntity.CashPrice(
            value = jsonObject.getInt("value"),
            includeTaxes = jsonObject.getBoolean("includeTaxes")
        )
    }

    private fun parseFinancedPrice(jsonObject: JSONObject): AdEntity.FinancedPrice {
        return AdEntity.FinancedPrice(
            value = jsonObject.getInt("value")
        )
    }

    private fun parseLocation(jsonObject: JSONObject): AdEntity.Location {
        val provinceObject = jsonObject.getJSONObject("province")
        val cityObject = jsonObject.getJSONObject("city")
        return AdEntity.Location(
            province = AdEntity.Province(
                id = provinceObject.getInt("id"),
                name = provinceObject.getString("name"),
                slug = provinceObject.getString("slug")
            ),
            city = AdEntity.City(
                id = cityObject.getInt("id"),
                name = cityObject.getString("name"),
                slug = cityObject.getString("slug")
            )
        )
    }

    private fun parseOrigin(jsonObject: JSONObject): AdEntity.Origin {
        return AdEntity.Origin(
            name = jsonObject.getString("name"),
            provider = jsonObject.getString("provider")
        )
    }

    private fun parseImages(jsonArray: JSONArray): List<String> {
        val images = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            images.add(jsonArray.getString(i))
        }
        return images
    }

    private fun parseBreadcrumbs(jsonObject: JSONObject): AdEntity.Breadcrumbs {
        val urlsArray = jsonObject.getJSONArray("urls")
        val urls = mutableListOf<AdEntity.Url>()
        for (i in 0 until urlsArray.length()) {
            val urlObject = urlsArray.getJSONObject(i)
            val url = AdEntity.Url(
                url = urlObject.getString("url"),
                label = urlObject.getString("label")
            )
            urls.add(url)
        }

        val breadcrumbJsonList = jsonObject.getJSONObject("breadcrumbJsonList")
        val itemListElementArray = breadcrumbJsonList.getJSONArray("itemListElement")
        val itemListElement = mutableListOf<AdEntity.ListItem>()
        for (i in 0 until itemListElementArray.length()) {
            val listItemObject = itemListElementArray.getJSONObject(i)
            val listItem = AdEntity.ListItem(
                type = listItemObject.getString("@type"),
                position = listItemObject.getInt("position"),
                name = listItemObject.getString("name"),
                item = listItemObject.getString("item")
            )
            itemListElement.add(listItem)
        }

        return AdEntity.Breadcrumbs(
            urls = urls,
            scriptTagType = jsonObject.getString("scriptTagType"),
            breadcrumbJsonList = AdEntity.BreadcrumbJsonList(
                context = breadcrumbJsonList.getString("@context"),
                type = breadcrumbJsonList.getString("@type"),
                itemListElement = itemListElement
            )
        )
    }

    private fun parseAttributes(jsonArray: JSONArray): List<AdEntity.Attribute> {
        val attributes = mutableListOf<AdEntity.Attribute>()
        for (i in 0 until jsonArray.length()) {
            val attributeObject = jsonArray.getJSONObject(i)
            val attribute = AdEntity.Attribute(
                type = attributeObject.getString("type"),
                fieldFormatted = attributeObject.getString("fieldFormatted"),
                value = attributeObject.getString("value"),
                valueFormatted = attributeObject.getString("valueFormatted")
            )
            attributes.add(attribute)
        }
        return attributes
    }

    private fun parseExtras(jsonArray: JSONArray): List<String> {
        val extras = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            extras.add(jsonArray.getString(i))
        }
        return extras
    }

    private fun parseStats(jsonObject: JSONObject): AdEntity.Stats {
        return AdEntity.Stats(
            listings = jsonObject.getInt("listings"),
            favorites = jsonObject.getInt("favorites")
        )
    }
}
