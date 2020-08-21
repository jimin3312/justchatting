package com.example.justchatting.ui.friend.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.justchatting.R

class CustomFragment : Fragment() {

    var mText : String = ""
    companion object{
        fun createInstance(text : String) : CustomFragment
        {
            var customFragment = CustomFragment()
            customFragment.mText = text
            return customFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment, container, false)
        val textView = view.findViewById<TextView>(R.id.dialog_textview_detail)
        textView.text = mText


        return view
    }
}