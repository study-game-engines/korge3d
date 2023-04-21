package korlibs.korge3d

import korlibs.datastructure.FastArrayList
import korlibs.kmem.*
import korlibs.graphics.*
import korlibs.graphics.shader.Program
import korlibs.graphics.shader.VertexLayout

data class BufferWithVertexLayout(
    val buffer: Buffer,
    val layout: VertexLayout
) {
    val vertexSizeInBytes = layout.totalSize
    val vertexSizeInFloats = vertexSizeInBytes / 4
}

@Korge3DExperimental
data class Mesh3D constructor(
    val vertexBuffers: FastArrayList<BufferWithVertexLayout>,
    val indexBuffer: Buffer,
    val indexType: AGIndexType,
    val vertexCount:Int,
    val program: Program?,
    val drawType: AGDrawType,
    val hasTexture: Boolean = false,
    val maxWeights: Int = 0,
    var skin: Skin3D? = null,
    var material: Material3D? = null
) {
    //val modelMat = Matrix3D()
    //val vertexCount = vertexBuffer.size / 4 / vertexSizeInFloats

    /*

    val buffer by lazy {
        Buffer.alloc(data.size * 4).apply {
            setAlignedArrayFloat32(0, this@Mesh3D.data, 0, this@Mesh3D.data.size)
        }
        //Buffer.wrap(MemBufferAlloc(data.size * 4)).apply {
        //	arraycopy(this@Mesh3D.data, 0, this@apply.mem, 0, this@Mesh3D.data.size) // Bug in kmem-js?
        //}
    }

     */

    init {
        //println("vertexCount: $vertexCount, vertexSizeInFloats: $vertexSizeInFloats, data.size: ${data.size}")
    }
}