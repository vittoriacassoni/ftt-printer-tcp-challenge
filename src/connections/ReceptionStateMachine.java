package connections;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.LinkedList;

public class ReceptionStateMachine {
    private EnumstateMaquina state;
    private StringBuilder content;
    private MessageTO message;
    private final Charset iso88591charset  = Charset.forName("ISO-8859-1");


    public List<MessageTO> prepareData(byte[] datas, int length) {
        List<MessageTO> result = new LinkedList<>();
        for (int i= 0; i< length; i++) {
            switch (state) {
                case AGUARDA_STX:
                    if (datas[i] == 0x02) {
                        state = EnumstateMaquina.AGUARDA_OPCODE;
                    }
                    break;
                case AGUARDA_OPCODE:
                    message.setOpCode(new String(datas,i,1));
                    state = EnumstateMaquina.AGUARDA_ETX;
                    break;
                case AGUARDA_ETX:
                    if (datas[i] == 0x03) {
                        message.setMensagem(content.toString());
                        result.add(message);
                        initialize();
                    }
                    else{
                        ByteBuffer bb = ByteBuffer.wrap(new byte[]{datas[i]});
                        content.append(iso88591charset .decode(bb).toString());
                    }
                    break;
                default:
                    state = EnumstateMaquina.AGUARDA_ETX;
                    break;
            }
        }
        return result;
    }

    public void initialize() {
        content = new StringBuilder();
        message = new MessageTO();
        state = EnumstateMaquina.AGUARDA_STX;
    }
}
