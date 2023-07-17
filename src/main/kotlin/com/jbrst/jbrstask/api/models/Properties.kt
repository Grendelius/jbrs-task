package com.jbrst.jbrstask.api.models


data class PropertiesDto(val property: List<PropertyDto>)

data class PropertyDto(val name: String, val value: Any?)