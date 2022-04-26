package com.example.hm3_retrofit.fragments

import retrofit2.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hm3_retrofit.adapter.PersonAdapter
import com.example.hm3_retrofit.databinding.FragmentListBinding
import com.example.hm3_retrofit.model.CartoonPerson
import com.example.hm3_retrofit.model.ResponceApi
import com.example.hm3_retrofit.retrofit.RickMortyService


class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }


    private val myAdapter = PersonAdapter()
    private var currentRequest: Call<ResponceApi>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentListBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //https://youtu.be/IDVxFjLeecA?t=12449

        val request = RickMortyService.personApi.getUsers()
        request.enqueue(object : Callback<ResponceApi> {
            override fun onResponse(call: Call<ResponceApi>, response: Response<ResponceApi>) {
                if (response.isSuccessful) {
                    val persons = response.body()?.results

                    with(binding) {
                        myAdapter.submitList(persons)
                        recyclerView.adapter = myAdapter
                        recyclerView.layoutManager = LinearLayoutManager(view.context)
                    }
                } else {
                    HttpException(response).message()
                }
                currentRequest = null
            }

            override fun onFailure(call: Call<ResponceApi>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT)
                    .show()
                currentRequest = null

            }
        })
        currentRequest = request
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        currentRequest?.cancel()
    }
}