package veiculos;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Tiziano on 15/12/2014.
 */
public class VeiculoFunctions {

    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "");
    }

    public static TextWatcher insert(final String mask, final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                String str = VeiculoFunctions.unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                Editable editable = ediTxt.getText();
                isUpdating = true;
                ediTxt.setText(mascara);
                afterTextChanged(editable);
                ediTxt.setSelection(mascara.length());
            }


            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {
                if(mask.equals("###-####")){
                    if(s.length()<3){
                        ediTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                    }else if(s.length()>=3){
                        ediTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                }if(mask.equals("##/##/####")){
                    ediTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
                }

            }

        };
    }
}
