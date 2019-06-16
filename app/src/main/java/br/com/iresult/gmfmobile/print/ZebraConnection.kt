package br.com.iresult.gmfmobile.print

import android.bluetooth.BluetoothAdapter
import android.util.Log
import com.zebra.sdk.comm.BluetoothConnection
import com.zebra.sdk.comm.ConnectionException
import com.zebra.sdk.printer.PrinterLanguage
import com.zebra.sdk.printer.ZebraPrinter
import com.zebra.sdk.printer.ZebraPrinterFactory
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException
import io.reactivex.Observable
import android.content.Intent
import android.bluetooth.BluetoothDevice
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import com.zebra.sdk.comm.Connection
import com.zebra.sdk.printer.ZebraPrinterLinkOs




/**
 * Created by daflecardoso 16/05/2019
 */

class ZebraConnection {

    var printerConnection: BluetoothConnection? = null
    var zebraPrinter: ZebraPrinter? = null
    var connected: Boolean  = false
    fun isPortOpen(): Boolean {

        try {
            connected = false
            connect().subscribe( { c ->  Log.i("ZebraConnection", "Conectado") }, { throwable ->    } )
            return connected
        } catch (e: java.lang.Exception){
            return false
        }
        return false;
    }
    fun connect(): Observable<BluetoothConnection> {

        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val pairedDevices = mBluetoothAdapter.bondedDevices

        return Observable.create<BluetoothConnection> {

            Log.w("ZebraConnection", "Connectando")
            this.printerConnection = BluetoothConnection(pairedDevices.first().address)

            try {
                printerConnection?.open()
                Log.i("", "Conected")
                connected  = true;
            } catch (e: ConnectionException) {
                Log.e("ZebraConnection", "Comm Error! Disconnecting")
                disconnect()
            } catch (e: Exception) {
                Log.e("ZebraConnection", "Comm Error! Disconnecting")
                disconnect()
                throw e
            }

            printerConnection?.let { connection ->

                if (connection.isConnected) {

                    try {
                        this.zebraPrinter = ZebraPrinterFactory.getInstance(printerConnection)
                        Log.i("ZebraConnection","Determining Printer Language")
                        val pl = zebraPrinter?.printerControlLanguage
                        Log.i("ZebraConnection","Printer Language $pl")
                        it.onNext(connection)
                    } catch (e: ConnectionException) {
                        Log.i("ZebraConnection","Unknown Printer Language $e")
                        disconnect()
                        it.onError(e)
                        throw e
                    } catch (e: ZebraPrinterLanguageUnknownException) {
                        Log.i("ZebraConnection","Unknown Printer Language $e")
                        disconnect()
                        it.onError(e)
                        throw e
                    }
                }
            }
            it.onError(Throwable("Unknown error"))
        }
    }


    fun disconnect() {
        try {
            Log.i("ZebraConnection","Disconnecting")
            printerConnection?.close()
            Log.i("ZebraConnection","Not Connected")
        } catch (e: ConnectionException) {
            Log.i("ZebraConnection","COMM Error! Disconnected")
        } finally {
            //TODO DO ANYTHING
        }
    }

    fun printTest() {
        try {
            val configLabel = getConfigLabel()

            printerConnection?.write(configLabel)
            Log.i("ZebraConnection","Sending Data")
            if (printerConnection is BluetoothConnection) {
                val friendlyName = (printerConnection as BluetoothConnection).friendlyName
                Log.i("ZebraConnection", friendlyName)
            }
        } catch (e: ConnectionException) {
            Log.i("ZebraConnection", "Error ${e.message}")
        } finally {
            disconnect()
        }
    }

    /*
    * Returns the command for a test label depending on the printer control language
    * The test label is a box with the word "TEST" inside of it
    *
    * _________________________
    * |                       |
    * |                       |
    * |        TEST           |
    * |                       |
    * |                       |
    * |_______________________|
    *
    *
    */
    private fun getConfigLabel(): ByteArray? {
        val printerLanguage = zebraPrinter?.printerControlLanguage

        var configLabel: ByteArray? = null
        if (printerLanguage == PrinterLanguage.ZPL) {
            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".toByteArray()
        } else if (printerLanguage == PrinterLanguage.CPCL) {
            val cpclConfigLabel = "! 0 200 200 406 1\r\n" + "ON-FEED IGNORE\r\n" + "BOX 20 20 380 380 8\r\n" + "T 0 6 137 177 TEST\r\n" + "PRINT\r\n"
            configLabel = cpclConfigLabel.toByteArray()
        }
        return configLabel
    }
}