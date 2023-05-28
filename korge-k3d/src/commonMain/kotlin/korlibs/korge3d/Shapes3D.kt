package korlibs.korge3d

import korlibs.korge3d.internal.vector3DTemps
import korlibs.korge3d.util.*
import korlibs.math.geom.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Korge3DExperimental
fun Container3D.shape3D(width: Float = 1f, height: Float = 1f, depth: Float = 1f, drawCommands: MeshBuilder3D.() -> Unit): Shape3D {
   return  Shape3D(width, height, depth, drawCommands).addTo(this)
}

/*
 * Note: To draw solid quads, you can use [Bitmaps.white] + [AgBitmapTextureManager] as texture and the [colorMul] as quad color.
 */
@Korge3DExperimental
class Shape3D(
    initWidth: Float, initHeight: Float, initDepth: Float,
    drawCommands: MeshBuilder3D.() -> Unit
) : ViewWithMesh3D(createMesh(drawCommands).copy()) {

    var width: Float = initWidth
    var height: Float = initHeight
    var depth: Float = initDepth

    override fun prepareExtraModelMatrix(mat: MMatrix3D) {
        mat.identity().scale(width, height, depth)
    }

    companion object {

        fun createMesh(drawCommands: MeshBuilder3D.() -> Unit) = MeshBuilder3D {
            drawCommands()
        }
    }
}


@Korge3DExperimental
inline fun Container3D.cube(width: Int, height: Int, depth: Int, callback: Cube3D.() -> Unit = {}): Cube3D = cube(width.toFloat(), height.toFloat(), depth.toFloat(), callback)

@Korge3DExperimental
inline fun Container3D.cube(
    width: Float = 1f,
    height: Float = width,
    depth: Float = height,
    callback: Cube3D.() -> Unit = {}
): Cube3D = Cube3D(width, height, depth).addTo(this, callback)

@Korge3DExperimental
abstract class BaseViewWithMesh3D(mesh: Mesh3D) : ViewWithMesh3D(mesh.copy()) {
    var material: Material3D?
        get() = mesh.material
        set(value) {
            mesh.material = value
            invalidateRender()
        }
}

fun <T : BaseViewWithMesh3D> T.material(material: Material3D?): T {
    this.material = material
    return this
}

@Korge3DExperimental
class Cube3D(var width: Float, var height: Float, var depth: Float) : BaseViewWithMesh3D(mesh) {
    override fun prepareExtraModelMatrix(mat: MMatrix3D) {
        mat.identity().scale(width, height, depth)
    }

    companion object {
        val mesh = MeshBuilder3D {
            vector3DTemps {
                fun face(pos: Vector3) {
                    val dims = (0 until 3).filter { pos[it] == 0f }
                    val normal = Vector3.func { if (pos[it] != 0f) 1f else 0f }
                    val dirs = Array(2) { dim -> Vector3.func { if (it == dims[dim]) .5f else 0f } }
                    val dx = dirs[0]
                    val dy = dirs[1]

                    val i0 = addVertex(pos - dx - dy, normal, Vector2(0f, 0f))
                    val i1 = addVertex(pos + dx - dy, normal, Vector2(1f, 0f))
                    val i2 = addVertex(pos - dx + dy, normal, Vector2(0f, 1f))

                    val i3 = addVertex(pos - dx + dy, normal, Vector2(0f, 1f))
                    val i4 = addVertex(pos + dx - dy, normal, Vector2(1f, 0f))
                    val i5 = addVertex(pos + dx + dy, normal, Vector2(1f, 1f))

                    addIndices(i0, i1, i2, i3, i4, i5)
                }

                face(Vector3(0f, +.5f, 0f))
                face(Vector3(0f, -.5f, 0f))

                face(Vector3(+.5f, 0f, 0f))
                face(Vector3(-.5f, 0f, 0f))

                face(Vector3(0f, 0f, +.5f))
                face(Vector3(0f, 0f, -.5f))
            }
        }
    }
}

@Korge3DExperimental
inline fun Container3D.sphere(radius: Int, callback: Sphere3D.() -> Unit = {}): Sphere3D = sphere(radius.toFloat(), callback)

@Korge3DExperimental
inline fun Container3D.sphere(
    radius: Float = 1f,
    callback: Sphere3D.() -> Unit = {}
): Sphere3D = Sphere3D(radius).addTo(this, callback)

@Korge3DExperimental
class Sphere3D(var radius: Float) : BaseViewWithMesh3D(mesh) {
    override fun prepareExtraModelMatrix(mat: MMatrix3D) {
        mat.identity().scale(radius, radius, radius)
    }

    companion object {
        private const val PIf = PI.toFloat()

        val mesh = MeshBuilder3D {
            val N = 16
            val M = 16

            for (m in 0..M) {
                for (n in 0..N) {
                    val p = Vector3(
                        (sin(PIf * m/M) * cos(2 * PIf * n/N)) / 2f,
                        (sin(PIf * m/M) * sin(2 * PIf * n/N)) / 2f,
                        (cos(PIf * m/M)) / 2f
                    )
                    val nv = p.normalized()

                    addVertex(p, nv, Vector2(0f, 0f))
                }
            }

            for (m in 1 .. M) {
                val row0 = (m - 1) * N
                val row1 = (m + 0) * N
                for (n in 0..N) {
                    val r0 = row0 + n
                    val r1 = row1 + n
                    addIndices(r0 + 0, r0 + 1, r1 + 0)
                    addIndices(r0 + 1, r1 + 1, r1 + 0)
                }
            }
        }
    }
}
