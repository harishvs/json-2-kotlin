package com.fractalwrench.json2kotlin

import com.google.gson.annotations.SerializedName
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

class GsonBuildDelegate: SourceBuildDelegate {

    // TODO pattern compilation
    // FIXME pattern super hacky
    // TODO should pass TypedJsonElement (and as much info as possible,
    // maybe in a wrapper class e.g. PropBuildParams/ClassBuildParams)

    override fun prepareClassProperty(propertyBuilder: PropertySpec.Builder,
                                      kotlinIdentifier: String,
                                      jsonKey: String?) {
        if (kotlinIdentifier != jsonKey && jsonKey != null) {
            val serializedNameBuilder = AnnotationSpec.builder(SerializedName::class)
            serializedNameBuilder.addMember("value=\"${jsonKey.replace("%".toRegex(), "%%")}\"", "")
            propertyBuilder.addAnnotation(serializedNameBuilder.build())
        }
    }

    override fun prepareClass(classBuilder: TypeSpec.Builder,
                              kotlinIdentifier: String,
                              jsonElement: TypedJsonElement) {
    }

}