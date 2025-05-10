@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.data.utils

import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry
import kotlin.uuid.Uuid
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import kotlin.uuid.ExperimentalUuidApi

class UuidCodec : Codec<Uuid> {
    override fun encode(writer: BsonWriter, value: Uuid, encoderContext: EncoderContext) {
        writer.writeString(value.toString())
    }

    override fun decode(reader: BsonReader, decoderContext: DecoderContext): Uuid {
        return Uuid.parse(reader.readString())
    }

    override fun getEncoderClass(): Class<Uuid> {
        return Uuid::class.java
    }
}

class UuidCodecProvider : CodecProvider {
    override fun <T : Any> get(clazz: Class<T>, registry: CodecRegistry): Codec<T>? {
        return if (clazz == Uuid::class.java) {
            @Suppress("UNCHECKED_CAST")
            UuidCodec() as Codec<T>
        } else {
            null
        }
    }
}