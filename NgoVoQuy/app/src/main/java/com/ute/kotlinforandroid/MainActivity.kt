package com.ute.kotlinforandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ute.kotlinforandroid.databinding.ActivityMainBinding
import com.ute.kotlinforandroid.utils.*
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    // ── 1. Gom nhóm thao tác hiển thị với 'with(binding)' ─────
    private fun displayStudent(name: String, gpa: Double, email: String) {
        // Bên trong with(binding), mọi View thuộc binding đều là 'this'
        with(binding) {
            tvName.text = name
            tvGpa.text = "Điểm tích lũy: $gpa"
            tvEmail.text = email
            btnUpdate.isEnabled = true
            progressBar.gone() // Sử dụng Extension function đã viết
        }
    }

    // ── 2. Cấu hình Intent hoặc View mới với 'apply' ──────────
    private fun openDetailActivity(studentId: String) {
        val detailIntent = Intent(this, DetailActivity::class.java).apply {
            putExtra("KEY_STUDENT_ID", studentId)
            putExtra("KEY_TIMESTAMP", System.currentTimeMillis())
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(detailIntent)
    }

    // ── 3. Kiểm tra Null Safety với Safe Call ?.let ──────────
    private fun processAvatarUri(avatarUri: Uri?) {
        // Khối lệnh chỉ chạy khi avatarUri KHÁC NULL
        avatarUri?.let { validUri ->
            binding.imgAvatar.setImageURI(validUri)
            binding.tvAvatarStatus.text = "Đã tải ảnh đại diện!"
            toast("Ảnh đã được cập nhật")
        } ?: run {
            // Chạy khi avatarUri == null
            binding.imgAvatar.setImageResource(R.drawable.ic_default_avatar)
        }
    }

    // ── 4. Chèn hành động phụ (Side-Effects) với 'also' ──────────
    private fun calculateAndAudit(rawScore: Double): Double {
        return (rawScore * 10.0 / 4.0)
            .also { finalScore ->
                Log.d("STUDENT_AUDIT", "Điểm hệ 10 quy đổi: $finalScore")
            }
            .also {
                toast("Đã tính xong điểm: $it")
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvWelcome.text = "Chào mừng bạn đến với ViewBinding!"

        // Gọi thử các hàm thực hành
        displayStudent(name = "Ngô Võ Quý", gpa = 3.2, email = "ngovoquy2006@gmail.com")
        processAvatarUri(null) // Test với null để gán icon mặc định
        calculateAndAudit(3.8)
        val currentGpa = 3.85
        binding.tvRanking.text = "Xếp loại: ${currentGpa.toAcademicRanking()}"
        toast("Chào mừng bạn! Điểm: ${currentGpa.toAcademicRanking()}")
    }
}