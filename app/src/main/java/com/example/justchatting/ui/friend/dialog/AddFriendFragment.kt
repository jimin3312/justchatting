package com.example.justchatting.ui.friend.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.justchatting.R

class AddFriendFragment(text : String) : Fragment() {

    private var mText = text
    private var addFriendFragmentListener: OnAddFriendFragmentButtonListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment, container, false)
        val textView = view.findViewById<TextView>(R.id.dialog_textview_detail)
        val addButton = view.findViewById<Button>(R.id.btn_add)
        val cancelButton = view.findViewById<Button>(R.id.btn_cancel)
        textView.text = mText

        addButton.setOnClickListener { view->
            addFriendFragmentListener?.messageFromAddFriendFragment(true)
        }
        cancelButton.setOnClickListener{view->
            addFriendFragmentListener?.messageFromAddFriendFragment(false)
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(parentFragment is OnAddFriendFragmentButtonListener){
            addFriendFragmentListener = parentFragment as OnAddFriendFragmentButtonListener
        }
    }

    override fun onDetach() {
        super.onDetach()
        addFriendFragmentListener = null
    }
    interface OnAddFriendFragmentButtonListener{
        fun messageFromAddFriendFragment(isAdd : Boolean)
    }

}