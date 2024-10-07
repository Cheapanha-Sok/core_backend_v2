package com.panha.core_backend.utilities


import com.panha.core_backend.core.BaseEntity
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Component
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

@Component
class UtilService {

    private fun getIgnoreField(include: List<String>?, exclude: List<String>?): Array<String> {
        // Retrieve declared fields of the BaseEntity class
        val baseEntityProperties = BaseEntity::class.java.declaredFields.map { it.name }.toMutableList()

        // Exclude fields from the provided exclude list
        exclude?.let { baseEntityProperties.removeAll(it) }

        // Include additional fields from the include list
        include?.let { baseEntityProperties.addAll(it) }

        // Convert the list to an array
        return baseEntityProperties.toTypedArray()
    }

    fun <T : Any> bindProperties(t: T, t1: T, include: List<String>? = null, exclude: List<String>? = null) {
        // Get the fields to be ignored during the copy
        val ignoreFields = getIgnoreField(include, exclude)

        // Copy properties from t to t1, ignoring specified fields
        BeanUtils.copyProperties(t, t1, *ignoreFields)
    }

    fun filterDateBetween(
        fieldName: String,
        startDate: String?,
        endDate: String?,
        cb: CriteriaBuilder,
        root: Root<*>
    ): Predicate? {
        // Check if both start and end dates are provided
        if (startDate != null && endDate != null) {
            val formatter = SimpleDateFormat("yyyy/MM/dd")
            return try {
                val start = formatter.parse(startDate)
                val end = formatter.parse(endDate)
                cb.between(root.get(fieldName), start, end)
            } catch (e: ParseException) {
                e.printStackTrace()
                null
            }
        }
        return null
    }

    fun checkStringValueIsNumeric(stringValue: String): Boolean {
        return try {
            stringValue.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun getValueFromField(obj: Any, targetField: String): String? {
        return try {
            val field = obj::class.java.getDeclaredField(targetField)
            field.isAccessible = true
            val value = field.get(obj)?.toString()
            field.isAccessible = false
            value
        } catch (e: Exception) {
            null
        }
    }

    fun setValueToField(obj: Any, fieldName: String, value: Any) {
        try {
            val field = obj::class.java.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(obj, value)
            field.isAccessible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun readInstanceProperty(instance: Any, propertyName: String): Any? {
        return try {
            val property = instance::class.memberProperties.firstOrNull { it.name == propertyName } as? KProperty1<Any, *>
            property?.get(instance)
        } catch (e: NoSuchElementException) {
            null
        }
    }

    fun getInstanceName(instance: Any): String? {
        return instance::class.simpleName?.toLowerCase(Locale.getDefault())
    }

    fun getInstanceName(instance: Class<*>): String {
        return instance.simpleName.toLowerCase(Locale.getDefault())
    }
}
