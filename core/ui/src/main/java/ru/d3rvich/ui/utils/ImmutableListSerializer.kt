package ru.d3rvich.ui.utils

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal class ImmutableListSerializer<T>(dataSerializer: KSerializer<T>) :
    KSerializer<ImmutableList<T>> {

    private val serializer = ListSerializer(dataSerializer)

    override val descriptor: SerialDescriptor = serializer.descriptor

    override fun serialize(encoder: Encoder, value: ImmutableList<T>) {
        encoder.encodeSerializableValue(serializer, value.toList())
    }

    override fun deserialize(decoder: Decoder): ImmutableList<T> {
        return decoder.decodeSerializableValue(serializer).toImmutableList()
    }
}