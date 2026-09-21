package com.example.ngovoquy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ngovoquy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Khởi tạo ViewBinding chuẩn mực
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Xử lý Edge-to-Edge tự động cho thanh điều hướng/trạng thái
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Thử nghiệm 'with': Gom nhóm thao tác cập nhật dữ liệu UI
        displayStudent(name = "Ngô Võ Quý", gpa = 3.65, email = "ngovoquy@ute.edu.vn")

        // 2. Thử nghiệm 'let' & 'run': Null-Safety cho ảnh đại diện
        processAvatarUri(null) // Truyền null để kích hoạt nhánh 'run'

        // 3. Thử nghiệm 'also': Tính điểm hệ 10 và in Logcat, Toast
        calculateAndAudit(3.65)

        // 4. Thử nghiệm 'apply': Thiết lập Intent khi bấm nút
        binding.btnUpdate.setOnClickListener {
            openDetailActivity("SV2026_01")
        }
    }

    // 1. Dùng 'with(binding)' để gom nhóm thao tác View
    private fun displayStudent(name: String, gpa: Double, email: String) {
        with(binding) {
            tvName.text = name
            tvGpa.text = "Điểm tích lũy: $gpa"
            tvEmail.text = email
            btnUpdate.isEnabled = true
            progressBar.visibility = View.GONE
        }
    }

    // 2. Dùng 'apply' để cấu hình Intent khởi tạo
    private fun openDetailActivity(studentId: String) {
        val detailIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("KEY_STUDENT_ID", studentId)
            putExtra("KEY_TIMESTAMP", System.currentTimeMillis())
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        toast("Đã áp dụng apply cấu hình Intent cho ID: $studentId")
        startActivity(detailIntent)
    }

    // 3. Dùng '?.let' kết hợp '?: run' để xử lý Null Safety
    private fun processAvatarUri(avatarUri: Uri?) {
        avatarUri?.let { validUri ->
            binding.imgAvatar.setImageURI(validUri)
            binding.tvAvatarStatus.text = "Đã tải ảnh đại diện!"
            toast("Ảnh đã được cập nhật")
        } ?: run {
            // Nhánh chạy khi avatarUri == null
            binding.imgAvatar.setImageResource(android.R.drawable.sym_def_app_icon)
            binding.tvAvatarStatus.text = "Dùng ảnh mặc định (Null Safe qua run)"
        }
    }

    // 4. Dùng 'also' chèn side-effect ghi log và hiện toast không gián đoạn luồng
    private fun calculateAndAudit(rawScore: Double): Double {
        return (rawScore * 10.0 / 4.0)
            .also { finalScore ->
                Log.d("STUDENT_AUDIT", "Điểm hệ 10 quy đổi: $finalScore")
            }
            .also {
                toast("Đã tính xong điểm: $it")
            }
    }

    // Hàm tiện ích hiển thị thông báo Toast nhanh
    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}