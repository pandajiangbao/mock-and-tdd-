package parking;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class InOrderParkingStrategyTest {

	@Test
    public void testCreateReceipt_givenACarAndAParkingLog_thenGiveAReceiptWithCarNameAndParkingLotName() {

	    /* Exercise 1, Write a test case on InOrderParkingStrategy.createReceipt()
	    * With using Mockito to mock the input parameter */
	    //given
        Receipt receipt = new Receipt();
        receipt.setCarName("aodi");
        receipt.setParkingLotName("panda");
        InOrderParkingStrategy inOrderParkingStrategy = new InOrderParkingStrategy();
        ParkingLot parkingLot = mock(ParkingLot.class);
        Car car = mock(Car.class);
        //when
        when(parkingLot.getName()).thenReturn("panda");
        when(car.getName()).thenReturn("aodi");
        Receipt strategyReceipt = inOrderParkingStrategy.createReceipt(parkingLot, car);
        //then
        Assert.assertEquals(receipt.getCarName(), strategyReceipt.getCarName());
        Assert.assertEquals(receipt.getParkingLotName(), strategyReceipt.getParkingLotName());
    }

    @Test
    public void testCreateNoSpaceReceipt_givenACar_thenGiveANoSpaceReceipt() {

        /* Exercise 1, Write a test case on InOrderParkingStrategy.createNoSpaceReceipt()
         * With using Mockito to mock the input parameter */
        //given
        Receipt receipt = new Receipt();
        receipt.setCarName("aodi");
        InOrderParkingStrategy inOrderParkingStrategy = new InOrderParkingStrategy();
        Car car = mock(Car.class);
        //when
        when(car.getName()).thenReturn("aodi");
        Receipt noSpaceReceipt = inOrderParkingStrategy.createNoSpaceReceipt(car);
        //then
        Assert.assertEquals(receipt.getCarName(), noSpaceReceipt.getCarName());
        Assert.assertEquals(ParkingStrategy.NO_PARKING_LOT, noSpaceReceipt.getParkingLotName());
    }

    @Test
    public void testPark_givenNoAvailableParkingLot_thenCreateNoSpaceReceipt(){

	    /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for no available parking lot */
        //given
        Receipt receipt = new Receipt();
        receipt.setCarName("aodi");
        receipt.setParkingLotName(ParkingStrategy.NO_PARKING_LOT);
        ArrayList<ParkingLot> list = new ArrayList<>();
        list.add(new ParkingLot("panda",0));
        Car car = mock(Car.class);
        //when
        when(car.getName()).thenReturn("aodi");
        InOrderParkingStrategy inOrderParkingStrategy = spy(InOrderParkingStrategy.class);
        Receipt noSpaceReceipt = inOrderParkingStrategy.park(list, car);
        //then

        Assert.assertEquals(receipt.getCarName(), noSpaceReceipt.getCarName());
        Assert.assertEquals(ParkingStrategy.NO_PARKING_LOT,noSpaceReceipt.getParkingLotName());
    }

    @Test
    public void testPark_givenThereIsOneParkingLotWithSpace_thenCreateReceipt(){

        /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for one available parking lot */
        //given
        Receipt receipt = new Receipt();
        receipt.setCarName("aodi");
        receipt.setParkingLotName("panda");
        ArrayList<ParkingLot> list = new ArrayList<>();
        list.add(new ParkingLot("panda",1));
        Car car = mock(Car.class);
        //when
        when(car.getName()).thenReturn("aodi");
        InOrderParkingStrategy inOrderParkingStrategy = spy(InOrderParkingStrategy.class);
        Receipt createReceipt = inOrderParkingStrategy.park(list, car);
        //then
        Assert.assertEquals(receipt.getCarName(), createReceipt.getCarName());
        Assert.assertEquals(receipt.getParkingLotName(),createReceipt.getParkingLotName());
    }

    @Test
    public void testPark_givenThereIsOneFullParkingLot_thenCreateReceipt(){

        /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for one available parking lot but it is full */
        //given
        Receipt receipt = new Receipt();
        receipt.setCarName("aodi");
        receipt.setParkingLotName("panda");
        ArrayList<ParkingLot> list = new ArrayList<>();
        list.add(new ParkingLot("panda",1));
        Car car = mock(Car.class);
        //when
        when(car.getName()).thenReturn("aodi");
        InOrderParkingStrategy inOrderParkingStrategy = spy(InOrderParkingStrategy.class);
        Receipt createReceipt = inOrderParkingStrategy.park(list, car);
        //then
        Assert.assertEquals(receipt.getCarName(), createReceipt.getCarName());
        Assert.assertEquals(receipt.getParkingLotName(),createReceipt.getParkingLotName());
    }

    @Test
    public void testPark_givenThereIsMultipleParkingLotAndFirstOneIsFull_thenCreateReceiptWithUnfullParkingLot(){

        /* Exercise 3: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for multiple parking lot situation */
        Receipt receipt = new Receipt();
        receipt.setCarName("aodi");
        receipt.setParkingLotName("eric");
        ArrayList<ParkingLot> list1 = new ArrayList<>();
        list1.add(new ParkingLot("panda",1));
        list1.get(0).getParkedCars().add(new Car("bill"));

        list1.add(new ParkingLot("eric",2));
        list1.get(1).getParkedCars().add(new Car("aaa"));
        Car car = mock(Car.class);
        //when
        when(car.getName()).thenReturn("aodi");
        InOrderParkingStrategy inOrderParkingStrategy = spy(InOrderParkingStrategy.class);
        Receipt createReceipt = inOrderParkingStrategy.park(list1, car);
        //then
        Mockito.verify(inOrderParkingStrategy,times(1)).park(list1,car);
        Assert.assertEquals(receipt.getCarName(), createReceipt.getCarName());
        Assert.assertEquals(receipt.getParkingLotName(),createReceipt.getParkingLotName());
    }


}
