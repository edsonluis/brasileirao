package br.edsonluis.app.brasileirao.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import br.edsonluis.app.brasileirao.BrasileiraoApplication;

public class Utils {

	private static final Context context = BrasileiraoApplication.getContext();

	private static SharedPreferences getSharedPreferences() {
		return context.getSharedPreferences(Constantes.BRASILEIRAO_PREFERENCE,
				Context.MODE_PRIVATE);
	}

	public static String abreviarNome(String nome) {
		return nome.replace("Paranaense", "PR").replace("Mineiro", "MG");
	}

	public static void saveTabelaJson(String json) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Constantes.JSON_TABELA, json);
		editor.commit();
	}

	public static String getTabelaJson() {
		return getSharedPreferences().getString(Constantes.JSON_TABELA, null);
	}

	public static void saveRodadaJson(String json, int rodada, boolean atual) {
		Editor editor = getSharedPreferences().edit();
		editor.putString(Constantes.JSON_RODADA + rodada, json);
		if (atual)
			editor.putInt(Constantes.RODADA_ATUAL, rodada);
		editor.commit();
	}

	public static String getRodadaJson(int rodada) {
		if (rodada != 0) {
			return getSharedPreferences().getString(
					Constantes.JSON_RODADA + rodada, null);
		} else {
			rodada = getSharedPreferences().getInt(Constantes.RODADA_ATUAL, 0);
			return (rodada != 0) ? getRodadaJson(rodada) : null;
		}

	}

//	private static Long getExpirationDate() {
//		Calendar c = Calendar.getInstance();
//		c.setTime(new Date());
//		c.add(Calendar.DATE, 1);
//		return c.getTimeInMillis();
//	}
//
//	private static boolean checkExpiration() {
//		Long expires_data = getSharedPreferences().getLong(
//				Constantes.EXPIRES_TABELA, 0);
//		if (expires_data != 0) {
//			Calendar expiration = Calendar.getInstance();
//			expiration.setTimeInMillis(expires_data);
//
//			Calendar today = Calendar.getInstance();
//
//			if (Constantes.DEBUG)
//				Log.d(Utils.class.getSimpleName(), "Expires: " + expires_data);
//
//			return (checkUpdateDate() && today.compareTo(expiration) > 0);
//		}
//		return true;
//	}

	public static boolean checkUpdateDate() {
		Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DAY_OF_WEEK);
		if (day == Calendar.SUNDAY || day == Calendar.WEDNESDAY
				|| day == Calendar.THURSDAY || day == Calendar.SATURDAY) {
			return true;
		}
		return false;
	}

	/**
	 * Checar se o dispositivo está conectado à internet.
	 * 
	 * @param context
	 *            Contexto.
	 * @return Verdadeiro caso esteja conectado, falso caso não esteja.
	 */
	public static boolean isOnline() {
		boolean result = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			result = true;
		}
		return result;
	}

	public static int convertPixelsToDp(float px) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return (int) dp;
	}

	public static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {

		} finally {
			try {
				is.close();
			} catch (IOException e) {

			}
		}
		return sb.toString();
	}
}
