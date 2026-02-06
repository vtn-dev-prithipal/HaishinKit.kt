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
     * Essential for preventing buffer corruption when same audio data is sent to multiple outputs.
     */
    fun copy(): MediaBuffer {
        val clonedPayload = payload?.let { originalBuffer ->
            // Save original position and limit
            val originalPosition = originalBuffer.position()
            val originalLimit = originalBuffer.limit()

            // Create new buffer with same capacity
            val cloned = ByteBuffer.allocateDirect(originalBuffer.capacity())

            // Copy data
            originalBuffer.rewind()
            cloned.put(originalBuffer)

            // Restore original buffer state
            originalBuffer.position(originalPosition)
            originalBuffer.limit(originalLimit)

            // Set cloned buffer to same state
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
