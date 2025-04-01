package com.smalog.util;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.smalog.constant.ApplicationConstants;
import com.smalog.constant.DBKeyLabelConstants;
import com.smalog.model.ValidationMessage;

import jakarta.servlet.http.HttpServletRequest;

/**
 * プログラムで使用する共通処理を定義する
 */
public class ApplicationUtils {
    
    /**
	 * BindingResultからエラーメッセージを取得し、Mapに変換して返却する
	 * 可変項目の場合は「test[0].code」「test[1].code」のような場合でも「Field（name）」属性は保持される　※要素を消しても詰められたりしない
	 * @param bindingResult	バリデーション結果
	 * @return
	 */
	public static Map<String, ValidationMessage> parseBindingResult(BindingResult bindingResult){
		Map<String, ValidationMessage> validationMessageList = new HashMap<>();
		ValidationMessage validationMessage;

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			String fieldId = fieldError.getField();
			String errorMessage = fieldError.getDefaultMessage();
			Object rejectedValue = fieldError.getRejectedValue();

			if(validationMessageList.containsKey(fieldId)){
				validationMessage = validationMessageList.get(fieldId);
				validationMessage.addErrorMessage(errorMessage);
			}else{
				validationMessage = new ValidationMessage(fieldId, errorMessage, rejectedValue);
				validationMessageList.put(fieldId, validationMessage);
			}
		}

		return validationMessageList;
	}

	/**
	 * AES暗号化
	 * nullの場合はnullを返却する
	 * @param plainText
	 * @return
	 */
	public static String paramEncrypt(Object plainText) {
		String result = null;
        try {
			if(plainText != null){
				String plainTextStr = plainText.toString();
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				SecretKeySpec keySpec = new SecretKeySpec(ApplicationConstants.AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
				cipher.init(Cipher.ENCRYPT_MODE, keySpec);
				byte[] encryptedBytes = cipher.doFinal(plainTextStr.getBytes(StandardCharsets.UTF_8));
				result = Base64.getUrlEncoder().encodeToString(encryptedBytes);
			}
			return result;
        } catch (Exception e) {
            throw new RuntimeException("AES暗号化に失敗しました", e);
        }
    }

	/**
	 * AES復号化
	 * nullの場合はnullを返却する
	 * @param encryptedText
	 * @return
	 */
	public static String paramDecrypt(String encryptedText) {
		String result = null;
        try {
			if(encryptedText != null){
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				SecretKeySpec keySpec = new SecretKeySpec(ApplicationConstants.AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
				cipher.init(Cipher.DECRYPT_MODE, keySpec);
				byte[] decryptedBytes = cipher.doFinal(Base64.getUrlDecoder().decode(encryptedText)); 
				result = new String(decryptedBytes, StandardCharsets.UTF_8);
			}
			return result;
        } catch (Exception e) {
            throw new RuntimeException("AES復号化に失敗しました", e);
        }
    }

	/**
	 * リクエストがajaxか
	 * @param request
	 * @return
	 */
	public static boolean isRequestAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

	/**
	 * セッターメソッドを文字列から呼び出す
	 * @param classObject
	 * @param methodName
	 * @param param
	 */
	public static void callVoidSetterInvoke(Object classObject, String methodName, Object param) {
		try {
            Method method = classObject.getClass().getMethod(methodName, String.class);
            method.invoke(classObject, param);
        } catch (Exception e) {
			throw new RuntimeException("リフレクションに失敗しました", e);
		}
	}

	/**
	 * ゲッターメソッドを文字列から呼び出す
	 * @param classObject
	 * @param methodName
	 * @param param
	 */
	public static <T> T callGetterInvoke(Object classObject, String methodName, Class<T> returnType) {
		T result = null;
		try {
			Method method = classObject.getClass().getMethod(methodName);
			result = returnType.cast(method.invoke(classObject));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ゲッターメソッドを文字列から呼び出す
	 * Stringで返す
	 * @param classObject
	 * @param methodName
	 */
	public static String callGetterInvokeToString(Object classObject, String methodName) {
		String result = null;
		try {
			Method method = classObject.getClass().getMethod(methodName);
			Object o = method.invoke(classObject);
			result = o == null ? null : String.valueOf(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
     * フォームのラジオボタンを生成する
     * @param labelTrue 1の場合
     * @param labelFalse 0の場合
     * @return
     */
    public static Map<String, Integer> getFormBooleanRadioButtonOptions(String labelTrue, String labelFalse){
        Map<String, Integer> radioButton = new LinkedHashMap<>();
        radioButton.put(labelTrue, DBKeyLabelConstants.INT_FLAG_ON.getIntValue());
        radioButton.put(labelFalse, DBKeyLabelConstants.INT_FLAG_OFF.getIntValue());
        return radioButton;
    }
	
	/**
	 * セレクトボックスの空のデータを作成する
	 */
	public static List<Map<String, String>> createFormSelectBoxEmptyDataList(){
        List<Map<String, String>> formSelectBoxDataList = new ArrayList<>();
		return formSelectBoxDataList;
    }

	/**
	 * セレクトボックスのレコードを生成する
	 * 全てString型で生成する
	 * @param text
	 * @param value
	 * @param dataId null許容
	 * @return
	 */
	public static Map<String, String> getFormSelectBoxRecord(String text, Object value, Object dataId){
        Map<String, String> selectBoxRecord = new HashMap<>();
        selectBoxRecord.put("text", text);
        selectBoxRecord.put("value", String.valueOf(value));
        selectBoxRecord.put("data-id", String.valueOf(dataId));
        return selectBoxRecord;
    }

	/**
	 * セレクトボックスの先頭にレコードを追加する
	 * @param formSelectBoxDataList
	 * @param text
	 * @param value
	 * @param dataId null許容
	 * @return
	 */
	public static List<Map<String, String>> insertFirstFormSelectBoxDataList(List<Map<String, String>> formSelectBoxDataList, String text, Object value, String dataId){
        Map<String, String> selectBoxRecord = getFormSelectBoxRecord(text, value, dataId);
		formSelectBoxDataList.add(0, selectBoxRecord);
        return formSelectBoxDataList;
    }
}