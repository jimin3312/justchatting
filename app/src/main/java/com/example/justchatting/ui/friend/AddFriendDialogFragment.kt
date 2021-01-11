package com.example.justchatting.ui.friend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.justchatting.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddFriendDialogFragment(tabPosition: Int, text: String) : DialogFragment() {

    private val viewModel: FriendViewModel by viewModel()
    private var mText = text
    private var mtabPosition = tabPosition
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment, container, false)
        val detailTextView = view.findViewById<TextView>(R.id.dialog_textview_detail)
        val addButton = view.findViewById<Button>(R.id.btn_add)
        val cancelButton = view.findViewById<Button>(R.id.btn_cancel)
        val inputEditTextView = view.findViewById<EditText>(R.id.dialog_edittext_input)
        detailTextView.text = mText

        addButton.setOnClickListener {
            Log.d("AddFriendFragment", "onclick add")
            when (mtabPosition) {
                TabPosition.PHONE.value -> {
                    Log.d("AddFriendFragment", "phone")
                    val re = Regex("[^A-Za-z0-9 ]")
                    val input = re.replace(inputEditTextView.text.toString(), "")
                    viewModel.addFriendWithPhoneNumber(input)
                }
                TabPosition.ID.value -> {
                    Log.d("AddFriendFragment", "id")
                    val re = Regex("[^A-Za-z0-9 ]")
                    val input = re.replace(inputEditTextView.text.toString(), "")
                    viewModel.addFriendWithId(input)
                }
            }
        }

        cancelButton.setOnClickListener { _ ->
            Log.d("AddFriendFragment", "onclick cancel")
            deleteAddFriendDialog()
        }

        return view
    }

    private fun deleteAddFriendDialog() {
        val fragment = requireParentFragment().parentFragmentManager.findFragmentByTag("dialog")
        if (fragment != null) {
            (fragment as DialogFragment).dismiss()
        }
    }

    enum class TabPosition(val value: Int) {
        PHONE(1),
        ID(2)
    }
}