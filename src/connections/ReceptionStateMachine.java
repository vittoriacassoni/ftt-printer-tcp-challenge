package connections;

import enums.MachineStateEnum;
import tos.MessageTO;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

public class ReceptionStateMachine {
    private MachineStateEnum state;
    private StringBuilder content;
    private MessageTO message;
    private final Charset iso88591charset  = Charset.forName("ISO-8859-1");


    public List<MessageTO> prepareData(byte[] datas, int length) {
        List<MessageTO> result = new LinkedList<>();
        for (int i= 0; i< length; i++) {
            switch (state) {
                case AGUARDA_STX:
                    if (datas[i] == 0x02) {
                        state = MachineStateEnum.AGUARDA_OPCODE;
                    }
                    break;
                case AGUARDA_OPCODE:
                    message.setOpCode(new String(datas,i,1));
                    state = MachineStateEnum.AGUARDA_ETX;
                    break;
                case AGUARDA_ETX:
                    if (datas[i] == 0x03) {
                        message.setMessage(content.toString());
                        result.add(message);
                        initialize();
                    }
                    else{
                        ByteBuffer bb = ByteBuffer.wrap(new byte[]{datas[i]});
                        content.append(iso88591charset .decode(bb).toString());
                    }
                    break;
                default:
                    state = MachineStateEnum.AGUARDA_ETX;
                    break;
            }
        }
        return result;
    }

    public void initialize() {
        content = new StringBuilder();
        message = new MessageTO();
        state = MachineStateEnum.AGUARDA_STX;
    }
}
