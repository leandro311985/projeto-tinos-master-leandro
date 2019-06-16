package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.occourence

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.adapters.ImageAdapter
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.LeituraViewModel
import br.com.iresult.gmfmobile.utils.addOnChangeListener
import kotlinx.android.synthetic.main.fragment_ocorrencia.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

const val CAMERA = 1

class OcorrenciaFragment : androidx.fragment.app.Fragment() {

    val viewModel : LeituraViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ocorrencia, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btBack.setOnClickListener {
            goBack()
        }

        btTipoOcorrencia.setOnClickListener {
            AlertDialog.Builder(requireContext())
                    .setTitle("Selecione o tipo de Ocorrência")
                    .setItems(viewModel.formatedOccourenceList()) { _, position ->
                        viewModel.selecionaTipoOcorrencia(position)
                    }.show()
        }

        btCancel.setOnClickListener {
            viewModel.limparOcorrencia()
            goBack()
        }

        btSave.setOnClickListener {
            viewModel.salvarOcorrencia()
            this.goBack()
        }

        btJustificativa.setOnClickListener {
            AlertDialog.Builder(requireContext())
                    .setTitle("Selecione a Justificativa")
                    .setItems(viewModel.formatedJustifyList()) { _, position ->
                        viewModel.selecionaJustificativa(position)
                    }.show()
        }

        setupBindings()

        etObservacoes?.addOnChangeListener {
            viewModel.observacaoOcorrenciaValueChanged(it)
        }

        addPhoto.setOnClickListener { cameraTask() }
    }

    private fun goBack() {
        NavHostFragment.findNavController(this).popBackStack()
    }

    private fun hasCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(requireContext(), Manifest.permission.CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(CAMERA)
    fun cameraTask() {

        if (hasCameraPermission()) {
            EasyImage.openCameraForImage(this, 0)
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "GMF deseja utilizar sua camera",
                    CAMERA,
                    Manifest.permission.CAMERA)
        }
    }

    private fun setupBindings() {

        viewModel.tipoOcorrenciaSelecionada.observe(viewLifecycleOwner, Observer { tipoOcorrencia ->
            btTipoOcorrencia?.text = tipoOcorrencia?.descricao ?: getString(R.string.fragment_occourence_title_button_type_occourence)
        })
//
//        viewModel.justificativasOcorrencia.observe(viewLifecycleOwner, Observer {
//
//        })

        viewModel.enableDisableJustify.observe(viewLifecycleOwner, Observer {
            btJustificativa.isEnabled = it
            etObservacoes.isEnabled = it
        })

        viewModel.justificativaSelecionada.observe(viewLifecycleOwner, Observer {
            btJustificativa.text = it?.descricao ?: getString(R.string.fragment_occourence_title_button_justify)
        })

        viewModel.observacaoOcorrencia.observe(viewLifecycleOwner, Observer {
            if (etObservacoes.text.toString() != it) {
                etObservacoes.setText(it)
            }
        })

        recyclerView.let {
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayout.HORIZONTAL, false)
            it.adapter = ImageAdapter { image -> viewModel.removeImageOccourence(image) }.also { adapter ->
                viewModel.imagensOcorrencia.observe(viewLifecycleOwner, Observer { images ->
                    adapter.setItems(images)
                })
            }
        }

        viewModel.isHabilitarCampoTexto.observe(viewLifecycleOwner, Observer {
            etObservacoes.isEnabled = it
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {
            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                e?.printStackTrace()
            }

            override fun onImagesPicked(imageFiles: List<File>, source: EasyImage.ImageSource, type: Int) {
                onPhotosReturned(imageFiles)
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                if (source == EasyImage.ImageSource.CAMERA_IMAGE) {
                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(requireContext())
                    photoFile?.delete()
                }
            }
        })
    }

    private fun onPhotosReturned(returnedPhotos: List<File>) {
        viewModel.addImagemOcorrencia(returnedPhotos[0])
    }

    override fun onDestroy() {
        EasyImage.clearConfiguration(requireContext())
        super.onDestroy()
    }
}