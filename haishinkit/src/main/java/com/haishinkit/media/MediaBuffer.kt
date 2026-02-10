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
            // Save original state
            val originalPosition = originalBuffer.position()
            val originalLimit = originalBuffer.limit()

            // Prepare original buffer for reading from start
            originalBuffer.rewind()

            // Create clone and copy all data
            val cloned = ByteBuffer.allocateDirect(originalBuffer.capacity())
            cloned.put(originalBuffer)

            // Restore original buffer state
            originalBuffer.position(originalPosition)
            originalBuffer.limit(originalLimit)

            // Prepare cloned buffer for reading: position=0, limit=amount of data
            cloned.flip()

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
