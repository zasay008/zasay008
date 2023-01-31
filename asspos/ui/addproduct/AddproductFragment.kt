package com.example.asspos.ui.addproduct

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.asspos.Manifest
import com.example.asspos.R
import com.example.asspos.databinding.FragmentAddproductBinding
import com.example.asspos.dataclass.ProductModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.net.URI

class AddproductFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var bar_pro: EditText
    private lateinit var name_pro: EditText
    private lateinit var cat_pro: EditText
    private lateinit var stk_pro: EditText
    private lateinit var pri_pro: EditText
    private lateinit var add_pro: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //เริ่มก๊อปตรงนี้ได้เลย
        val v: View = inflater.inflate(R.layout.fragment_addproduct, container, false)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        bar_pro = v.findViewById(R.id.barcode)
        name_pro = v.findViewById(R.id.namepro)
        cat_pro = v.findViewById(R.id.category_pro)
        stk_pro = v.findViewById(R.id.stock_pro)
        pri_pro = v.findViewById(R.id.price_pro)
        add_pro = v.findViewById(R.id.addbtn)

        add_pro.setOnClickListener {

            addproduct()
        }
        return v
    }

    private fun addproduct() {

        val bar_txt = bar_pro.text.toString()
        val name_txt = name_pro.text.toString()
        val cat_txt = cat_pro.text.toString()
        val stk_txt = stk_pro.text.toString()
        val pri_txt = pri_pro.text.toString()
        val user = auth.currentUser!!.uid
        /* val resultemmail = user.replace(".","")
        if (bar_txt.isEmpty() || name_txt.isEmpty() || cat_txt.isEmpty() || stk_txt.isEmpty() || pri_txt.isEmpty()){
            val productModel: ProductModel = ProductModel(bar_txt, name_txt, cat_txt, stk_txt, pri_txt)
            databaseReference.child(user).child("ProductModel").push().setValue(productModel)
            Toast.makeText(activity, name_txt + "ถูกเพิ่มเข้าไปสำเร็จ", Toast.LENGTH_SHORT).show()

        }*/
        if (bar_txt.isEmpty()){
            bar_pro.error = "กรุณาใส่รหัสสินค้า"
        }
        if (name_txt.isEmpty()){
            name_pro.error = "กรุณาใส่ชื่อสินค้า"
        }
        if (cat_txt.isEmpty()){
            cat_pro.error = "กรุณาใส่หมวดหมู่สินค้า"
        }
        if (stk_txt.isEmpty()){
            stk_pro.error = "กรุณาใส่จำนวนสินค้า"
        }
        if (pri_txt.isEmpty()){
            pri_pro.error = "กรุณาใส่ราคาสินค้า"
        } else {

            val productModel: ProductModel =
                ProductModel(bar_txt, name_txt, cat_txt, stk_txt, pri_txt)
            databaseReference.child(user).child("ProductModel").push().setValue(productModel)
                .addOnCompleteListener {
                    Toast.makeText(activity, name_txt + "ถูกเพิ่มเข้าไปสำเร็จ", Toast.LENGTH_SHORT)
                        .show()
                    bar_pro.text.clear()
                    name_pro.text.clear()
                    cat_pro.text.clear()
                    stk_pro.text.clear()
                    pri_pro.text.clear()
                }.addOnFailureListener { err ->
                    Toast.makeText(activity, "มีบางอย่างผิดพลาด", Toast.LENGTH_SHORT).show()
                }
        }
    }//ถึงแค่นี้แหละ
}
