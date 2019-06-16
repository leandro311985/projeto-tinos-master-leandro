package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.order

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.adapters.ImageAdapter
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.LeituraViewModel
import br.com.iresult.gmfmobile.utils.addOnChangeListener
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.occourence.CAMERA
import kotlinx.android.synthetic.main.fragment_ordem_servico.*
import kotlinx.android.synthetic.main.fragment_ordem_servico.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class OrdemServicoFragment : androidx.fragment.app.Fragment() {

    val viewModel : LeituraViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ordem_servico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btBack.setOnClickListener { this.goBack() }

        btCancel.setOnClickListener{ this.goBack() }

//        val ligacao: Ligacao = activity?.intent?.extras?.getParcelable(LeituraActivity.ARG_UNIDADE) ?: return
//        viewModel.setLigacao(ligacao)

        btSave.setOnClickListener {
            viewModel.salvarServico()
        }

//        activity?.intent?.extras?.getString(LeituraActivity.ARG_ROTEIRO)?.let {
//            viewModel.fetchOsType(it)
//        }

        tipoOS.setOnClickListener {
            AlertDialog.Builder(requireContext())
                    .setTitle("Selecione o tipo de Serviço")
                    .setItems(viewModel.servicos.value?.map { it.descricaoRubrica }?.toTypedArray()) { dialog, which ->
                        viewModel.selecionarServico(which)
                    }.show()
        }

        etObservacoes.addOnChangeListener {
            viewModel.onTextoServicoChanged(it)
        }

        addPhoto.setOnClickListener {
            cameraTask()
        }


        recyclerView.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = ImageAdapter { image -> viewModel.removeImageServiceOrder(image) }.also { adapter ->
                viewModel.imagensServico.observe(this, Observer { images ->
                    adapter.setItems(images)
                })
            }
        }

        setupBindings()
    }

    private fun goBack() {
        NavHostFragment.findNavController(this).popBackStack()
    }

    private fun hasCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(requireContext(), Manifest.permission.CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(CAMERA)
    fun cameraTask() {

        if (hasCameraPermission()) {

            EasyImage.openCameraForImage(this@OrdemServicoFragment, 0)

        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "GMF deseja utilizar sua camera",
                    CAMERA,
                    Manifest.permission.CAMERA)
        }
    }

    private fun setupBindings() {

        viewModel.unidade.observe(viewLifecycleOwner, Observer { unidade ->

        })

        viewModel.servicoSelecionado.observe(viewLifecycleOwner, Observer { servico ->

            if(servico != null) {

                view?.tipoOS?.text = servico.descricaoRubrica
                view?.etObservacoes?.isEnabled = true

            } else {

                view?.tipoOS?.text = "TIPO de O.S."
                view?.etObservacoes?.isEnabled = false

            }

        })

        viewModel.textoServico.observe(viewLifecycleOwner, Observer {

            if(etObservacoes.text.toString() != it) {
                etObservacoes.setText(it)
            }

        })

        viewModel.servicosLeitura.observe(viewLifecycleOwner, Observer {
            servicosLeitura.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            servicosLeitura.adapter = OrdemServicoAdapter(it) {
                viewModel.removerServico(it)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {
            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                //Some error handling
                e?.printStackTrace()
            }

            override fun onImagesPicked(imageFiles: List<File>, source: EasyImage.ImageSource, type: Int) {
                onPhotosReturned(imageFiles)
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA_IMAGE) {
                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(requireContext())
                    photoFile?.delete()
                }
            }
        })
    }

    private fun onPhotosReturned(returnedPhotos: List<File>) {
//        photos.addAll(returnedPhotos)
//        imagesAdapter.notifyDataSetChanged()
//        recyclerView.scrollToPosition(photos.size() - 1)
        viewModel.addImagemServico(returnedPhotos[0])
    }

    override fun onDestroy() {
        // Clear any configuration that was done!
        EasyImage.clearConfiguration(requireContext())
        super.onDestroy()
    }

}