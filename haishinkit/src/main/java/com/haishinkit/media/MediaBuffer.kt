package com.haishinkit.media

import java.nio.ByteBuffer

data class MediaBuffer(
    val type: MediaType,
    var index: Int,
    var payload: ByteBuffer? = null,
    var timestamp: Long = 0L,
    var sync: Boolean = false,
) {
    /**
     * Creates a deep copy of this MediaBuffer with cloned ByteBuffer payload.
     * Used to prevent buffer corruption when multiple outputs share the same buffer.
     */
    fun copy(): MediaBuffer {
        val clonedPayload = payload?.let { originalBuffer ->
            val originalPosition = originalBuffer.position()
            val originalLimit = originalBuffer.limit()
            val cloned = ByteBuffer.allocateDirect(originalBuffer.capacity())
            originalBuffer.rewind()
            cloned.put(originalBuffer)
            originalBuffer.position(originalPosition)
            originalBuffer.limit(originalLimit)
            cloned.position(originalPosition)
            cloned.limit(originalLimit)
            cloned
        }
        return MediaBuffer(
            type = this.type,
            index = this.index,
            payload = clonedPayload,
            timestamp = this.timestamp,
            sync = this.sync
        )
    }
}
