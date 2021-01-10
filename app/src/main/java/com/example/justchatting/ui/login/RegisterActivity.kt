package com.example.justchatting.ui.login

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.example.justchatting.R
import com.example.justchatting.base.BaseActivity
import com.example.justchatting.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.Observer

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 1000
    }

    val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.aleadyHaveAccountTextviewRegister.setOnClickListener {
            finish()
        }

        binding.selectPhotoImageViewRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PERMISSIONS_REQUEST_READ_CONTACTS)
        }

        viewModel.successSignUp.observe(this, Observer {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        })

        viewModel.errorToastMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS && resultCode == Activity.RESULT_OK && data != null) {
            viewModel.selectedPhotoUri = data.data

            val bitmap =
                if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        viewModel.selectedPhotoUri
                    )
                } else {
                    val source = ImageDecoder.createSource(
                        this.contentResolver,
                        viewModel.selectedPhotoUri!!
                    )
                    ImageDecoder.decodeBitmap(source)
                }

            binding.selectPhotoImageViewRegister.setImageBitmap(bitmap)
        }
    }

    @LayoutRes
    override fun getLayoutId() = R.layout.activity_register
}