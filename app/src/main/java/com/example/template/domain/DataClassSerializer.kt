package com.example.template.domain

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

interface StringDataClass {
    val value: String
}

open class StringDataClassSerializer<T : StringDataClass>(serialName: String, val createInstance: (String) -> T) : KSerializer<T> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(serialName, PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: T): Unit = encoder.encodeString(value.value)
    override fun deserialize(decoder: Decoder): T = createInstance(decoder.decodeString())
}
