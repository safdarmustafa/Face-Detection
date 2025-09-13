package com.example.facedetection

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.facedetection.databinding.ActivityMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btncamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, 123)
            } else {
                Toast.makeText(this, "Oops, something went wrong", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == RESULT_OK) {
            val extras = data?.extras
            val bitmap = extras?.get("data") as? Bitmap
            if (bitmap != null) {
                binding.capturedImageView.setImageBitmap(bitmap)  // Set the captured image to the ImageView
                detectFace(bitmap)
            }
        }
    }

    private fun detectFace(bitmap: Bitmap) {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        val detector = FaceDetection.getClient(options)
        val image = InputImage.fromBitmap(bitmap, 0)

        val result = detector.process(image)
            .addOnSuccessListener { faces ->
                var resultText = ""
                var i = 1
                for (face in faces) {
                    resultText += "Face Number: $i\n" +
                            "Smile: ${face.smilingProbability?.times(100)}%\n" +
                            "Left Eye Open: ${face.leftEyeOpenProbability?.times(100)}%\n" +
                            "Right Eye Open: ${face.rightEyeOpenProbability?.times(100)}%\n\n"
                    i++
                }
                if (faces.isEmpty()) {
                    Toast.makeText(this, "No Face Detected", Toast.LENGTH_SHORT).show()
                } else {
                    binding.resultTextView.text = resultText
                    Toast.makeText(this, resultText, Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Face detection failed", Toast.LENGTH_SHORT).show()
            }
    }
}
