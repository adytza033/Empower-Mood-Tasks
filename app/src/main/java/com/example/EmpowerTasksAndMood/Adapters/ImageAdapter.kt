import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.EmpowerTasksAndMood.Model.Images
import com.example.EmpowerTasksAndMood.R

class ImageAdapter(
    private val context: Context,
    private val images: List<Images>,
    private val onImageClick: (Images) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        val bitmap = BitmapFactory.decodeByteArray(image.ImagesData, 0, image.ImagesData.size)
        holder.imageView.setImageBitmap(bitmap)
        holder.imageView.setOnClickListener { onImageClick(image) }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.itemViewImage)
    }
}
